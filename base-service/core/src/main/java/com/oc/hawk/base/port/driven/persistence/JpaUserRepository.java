package com.oc.hawk.base.port.driven.persistence;

import com.google.common.collect.Lists;
import com.oc.hawk.api.constant.AccountHolder;
import com.oc.hawk.base.domain.model.user.User;
import com.oc.hawk.base.domain.model.user.UserId;
import com.oc.hawk.base.domain.model.user.UserRepository;
import com.oc.hawk.base.port.driven.persistence.po.DepartmentPo;
import com.oc.hawk.base.port.driven.persistence.po.UserPo;
import com.oc.hawk.common.utils.AccountHolderUtils;
import com.oc.hawk.common.utils.DomainPageUtils;
import com.oc.hawk.ddd.web.DomainPage;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.criteria.internal.OrderImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.Predicate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
interface UserPoRepository extends JpaRepositoryImplementation<UserPo, Long> {

    boolean existsByEmail(String email);

    boolean existsByAuthName(String authName);

    Optional<UserPo> findByAuthName(String key);

    Optional<UserPo> findByPhone(String key);

    Optional<UserPo> findByEmail(String key);

    boolean existsByPhone(String phone);
}

@Component
@RequiredArgsConstructor
public class JpaUserRepository implements UserRepository {
    private final UserPoRepository userPoRepository;
    private final DepartmentRepository departmentRepository;

    @Override
    public User getUser(UserId id) {
        return userPoRepository.getOne(id.getId()).toUser();
    }

    @Override
    public User getCurrent() {
        final AccountHolder accountHolder = AccountHolderUtils.getAccountHolder();
        if (accountHolder == null) {
            return null;
        }

        return getUser(new UserId(accountHolder.getId()));
    }

    @Override
    public User save(User user) {
        UserPo po = UserPo.createNew(user);

        final DepartmentPo departmentPo = departmentRepository.findById(user.getDepartmentId().getId()).orElse(null);
        po.setDepartment(departmentPo);

        userPoRepository.save(po);
        return po.toUser();
    }

    @Override
    public boolean isEmailExisted(String email) {
        return userPoRepository.existsByEmail(email);
    }

    @Override
    public boolean isAuthNameExisted(String authName) {
        return userPoRepository.existsByAuthName(authName);
    }

    @Override
    public void deleteUser(User user) {
        userPoRepository.deleteById(user.getId().getId());
    }

    @Override
    public List<User> queryUser(String key, List<Long> ids, Boolean departmentIgnore) {
        final List<UserPo> result = userPoRepository.findAll((root, query, criteriaBuilder) -> {
            query.orderBy(new OrderImpl(root.get("id"), false));
            List<Predicate> predicates = Lists.newArrayList();
            if (!CollectionUtils.isEmpty(ids)) {
                final List<Long> distinctedIds = ids.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
                predicates.add(criteriaBuilder.in(root.get("id")).value(distinctedIds));
            } else {
                if (departmentIgnore == null || !departmentIgnore) {
                    User user = getCurrent();
                    if (!user.getDepartment().isMaster()) {
                        predicates.add(criteriaBuilder.equal(root.get("department"), user.getDepartment()));
                    }
                }

                if (StringUtils.isNotEmpty(key)) {
                    predicates.add(criteriaBuilder.like(root.get("name"), '%' + key + '%'));
                }

            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });

        return result.stream().map(UserPo::toUser).collect(Collectors.toList());
    }

    @Override
    public DomainPage<User> queryUserPage(int page, int size, String key) {
        User user = getCurrent();
        final Page<UserPo> result = userPoRepository.findAll((root, query, criteriaBuilder) -> {
            query.orderBy(new OrderImpl(root.get("id"), false));
            List<Predicate> predicates = Lists.newArrayList();
            if (!user.getDepartment().isMaster()) {
                predicates.add(criteriaBuilder.equal(root.get("department").get("id"), user.getDepartment().getId().getId()));
            }

            if (StringUtils.isNotEmpty(key)) {
                predicates.add(criteriaBuilder.or(criteriaBuilder.like(root.get("name"), '%' + key + '%'), criteriaBuilder.like(root.get("email"), '%' + key + '%')));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }, PageRequest.of(page, size, Sort.by(Sort.Order.desc("id"))));
        return DomainPageUtils.create(result.map(UserPo::toUser));
    }

    @Override
    public User byAuthName(String key) {
        return userPoRepository.findByAuthName(key).map(UserPo::toUser).orElse(null);
    }

    @Override
    public User byPhone(String key) {
        return userPoRepository.findByPhone(key).map(UserPo::toUser).orElse(null);
    }

    @Override
    public User byEmail(String key) {
        return userPoRepository.findByEmail(key).map(UserPo::toUser).orElse(null);
    }

    @Override
    public boolean isPhoneExisted(String phone) {
        return userPoRepository.existsByPhone(phone);
    }
}
