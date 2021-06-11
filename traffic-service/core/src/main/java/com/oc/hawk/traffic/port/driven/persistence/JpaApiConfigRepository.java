package com.oc.hawk.traffic.port.driven.persistence;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.oc.hawk.api.constant.AccountHolder;
import com.oc.hawk.common.utils.AccountHolderUtils;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.*;
import com.oc.hawk.traffic.entrypoint.domain.model.httpresource.HttpPath;
import com.oc.hawk.traffic.entrypoint.domain.model.httpresource.HttpResource;
import com.oc.hawk.traffic.entrypoint.domain.model.trace.Trace;
import com.oc.hawk.traffic.entrypoint.domain.model.trace.TraceId;
import com.oc.hawk.traffic.port.driven.persistence.po.EntryPointConfigGroupPO;
import com.oc.hawk.traffic.port.driven.persistence.po.EntryPointConfigPO;
import com.oc.hawk.traffic.port.driven.persistence.po.EntryPointGroupManagerPO;
import com.oc.hawk.traffic.port.driven.persistence.po.TrafficTracePo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.criteria.internal.OrderImpl;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import javax.persistence.criteria.CriteriaBuilder.In;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
interface ApiConfigPoRepository extends JpaRepositoryImplementation<EntryPointConfigPO, Long> {

    @Override
    Optional<EntryPointConfigPO> findById(Long id);

    List<EntryPointConfigPO> findByGroupIdIn(List<Long> groupIdList);
}

@Component
@RequiredArgsConstructor
@Slf4j
public class JpaApiConfigRepository implements EntryPointConfigRepository {

    private static final Integer DAYS = 7;
    private final ApiConfigPoRepository apiConfigPoRepository;
    private final EntryPointConfigGroupPoRepository apiConfigGroupPoRepository;
    private final EntryPointGroupManagerPoRepository apiGroupManagerPoRepository;
    private final TrafficTracePoRepository trafficTracePoRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public EntryPointConfigID save(EntryPointConfig config) {
        EntryPointConfigPO apiConfigPo = EntryPointConfigPO.createBy(config);
        if (Objects.nonNull(config.getConfigId())) {
            apiConfigPo.setId(config.getConfigId().getId());
        }
        apiConfigPoRepository.save(apiConfigPo);
        return new EntryPointConfigID(apiConfigPo.getId());
    }

    @Override
    public EntryPointConfig byId(EntryPointConfigID apiConfigId) {
        Optional<EntryPointConfigPO> apiPo = apiConfigPoRepository.findById(apiConfigId.getId());
        if (Objects.isNull(apiPo) || apiPo.isEmpty()) {
            return null;
        }
        return apiPo.get().toEntryPointConfig();
    }

    @Override
    public EntryPointGroupID save(EntryPointConfigGroup group) {
        EntryPointConfigGroupPO groupPo = EntryPointConfigGroupPO.createBy(group);
        apiConfigGroupPoRepository.save(groupPo);
        return new EntryPointGroupID(groupPo.getId());
    }

    @Override
    public List<EntryPointConfig> byKey(EntryPointConfig config, List<EntryPointConfigGroup> groupList) {
        if (Objects.isNull(groupList) || groupList.isEmpty()) {
            return new ArrayList<>();
        }

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<EntryPointConfigPO> criteriaQuery = criteriaBuilder.createQuery(EntryPointConfigPO.class);
        Root<EntryPointConfigPO> fromObj = criteriaQuery.from(EntryPointConfigPO.class);

        Path<Long> path = fromObj.get("groupId");
        In<Long> inClause = criteriaBuilder.in(path);
        groupList.stream().map(obj -> obj.getGroupId().getId()).collect(Collectors.toList()).forEach(inClause::value);

        Predicate conditionName = criteriaBuilder.like(fromObj.get("apiName"), '%' + config.getDescription().getName() + '%');
        Predicate conditionDesc = criteriaBuilder.like(fromObj.get("apiDesc"), '%' + config.getDescription().getDesc() + '%');
        Predicate conditionPath = criteriaBuilder.like(fromObj.get("apiPath"), '%' + config.getHttpResource().getPath().getPath() + '%');

        Predicate conditionWhere = criteriaBuilder.and(inClause, criteriaBuilder.or(conditionName, conditionDesc, conditionPath));

        criteriaQuery.where(conditionWhere);
        List<EntryPointConfigPO> resultPoList = entityManager.createQuery(criteriaQuery).getResultList();

        return resultPoList.stream().map(EntryPointConfigPO::toEntryPointConfig).collect(Collectors.toList());
    }

    @Override
    public List<EntryPointConfigGroup> findAllGroup() {
        List<EntryPointConfigGroupPO> poList = apiConfigGroupPoRepository.findAll();

        return poList.stream().map(EntryPointConfigGroupPO::toEntryPointConfigGroup).collect(Collectors.toList());
    }

    @Override
    public EntryPointConfigGroup byId(EntryPointGroupID apiConfigGroupId) {
        EntryPointConfigGroupPO groupPo = apiConfigGroupPoRepository.findById(apiConfigGroupId.getId()).get();
        return groupPo.toEntryPointConfigGroup();
    }

    @Override
    public List<EntryPointConfigGroup> findGroups() {
        AccountHolder holder = AccountHolderUtils.getAccountHolder();

        EntryPointGroupManagerPO managerPo = null;
        if (Objects.nonNull(holder)) {
            managerPo = apiGroupManagerPoRepository.findByUserId(holder.getId());
        }

        List<EntryPointConfigGroup> allGroupList = findAllGroup();
        if (Objects.isNull(managerPo) || StringUtils.isBlank(managerPo.getGroupids())) {
            return allGroupList;
        }

        String groupids = managerPo.getGroupids();
        String[] groupList = groupids.split(",");

        List<EntryPointConfigGroup> apiGroupList = new ArrayList<EntryPointConfigGroup>();
        for (String groupIdStr : groupList) {
            Long groupId = Long.parseLong(groupIdStr);
            EntryPointConfigGroupPO apiConfigGroupPo = apiConfigGroupPoRepository.findById(groupId).get();
            apiGroupList.add(apiConfigGroupPo.toEntryPointConfigGroup());
        }
        allGroupList.removeAll(apiGroupList);
        return allGroupList;
    }

    @Override
    public void update(List<EntryPointGroupID> entryPointGroupIdList) {
        AccountHolder accountHolder = AccountHolderUtils.getAccountHolder();
        EntryPointGroupManagerPO managerPo = null;
        if (Objects.nonNull(accountHolder)) {
            managerPo = apiGroupManagerPoRepository.findByUserId(accountHolder.getId());
        }
        if (Objects.isNull(managerPo)) {
            managerPo = new EntryPointGroupManagerPO();
            managerPo.setUserId(accountHolder.getId());
        }
        List<Long> groupIdList = entryPointGroupIdList.stream().map(EntryPointGroupID::getId).collect(Collectors.toList());
        String groupids = Joiner.on(",").join(groupIdList);
        managerPo.setGroupids(groupids);
        managerPo.setCreateTime(LocalDateTime.now());
        managerPo.setUpdateTime(LocalDateTime.now());
        apiGroupManagerPoRepository.save(managerPo);
    }

    @Override
    public List<EntryPointConfigGroup> byIdList(List<EntryPointGroupID> groupIdList) {
        List<Long> groupList = groupIdList.stream().map(EntryPointGroupID::getId).collect(Collectors.toList());
        List<EntryPointConfigGroupPO> apiConfigGroupPoList = apiConfigGroupPoRepository.findByIdIn(groupList);
        return apiConfigGroupPoList.stream().map(EntryPointConfigGroupPO::toEntryPointConfigGroup).collect(Collectors.toList());
    }

    @Override
    public List<EntryPointConfig> byGroupIdList(List<EntryPointGroupID> groupIdList) {
        List<Long> groupList = groupIdList.stream().map(EntryPointGroupID::getId).collect(Collectors.toList());
        List<EntryPointConfigPO> apiConfigPoList = apiConfigPoRepository.findByGroupIdIn(groupList);
        return apiConfigPoList.stream().map(EntryPointConfigPO::toEntryPointConfig).collect(Collectors.toList());
    }

    @Override
    public List<EntryPointConfig> batchSave(EntryPointGroupID entryPointConfigGroupId, List<EntryPointConfig> apiConfigList) {
        try {
            List<EntryPointConfig> resultList = new ArrayList<>();
            for (EntryPointConfig apiConfig : apiConfigList) {
                EntryPointConfigPO apiConfigPo = EntryPointConfigPO.createBy(apiConfig);
                apiConfigPo.setGroupId(entryPointConfigGroupId.getId());
                apiConfigPoRepository.save(apiConfigPo);
                resultList.add(apiConfigPo.toEntryPointConfig());
            }
            return resultList;
        } catch (Exception e) {
            log.error("batchSaveException:{}", e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void saveTrace(List<Trace> traceList) {
        final List<TrafficTracePo> tracePos = traceList.stream().distinct().map(TrafficTracePo::createBy).collect(Collectors.toList());
        trafficTracePoRepository.saveAll(tracePos);
    }

    @Override
    public List<Trace> queryTraceInfoList(Integer page, Integer size, String key) {
        int pageSize = size == null ? 10 : size;
        int pageNum = page == null ? 0 : (page - 1) * pageSize;
        //多查一条
        pageSize += 1;
//
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<TrafficTracePo> criteriaQuery = cb.createQuery(TrafficTracePo.class);
        Root<TrafficTracePo> root = criteriaQuery.from(TrafficTracePo.class);

        List<Predicate> predicates = Lists.newArrayList(
                cb.equal(root.get("kind"), "server")
        );
        if (StringUtils.isNotEmpty(key)) {
            predicates.add(cb.or(
                    cb.equal(root.get("dstWorkload"), key),
                    cb.like(root.get("path"), key + "%")
            ));
        }

        criteriaQuery.orderBy(new OrderImpl(root.get("id"), false));
        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(criteriaQuery)
                .setFirstResult(pageNum)
                .setMaxResults(pageSize)
                .getResultList().stream().map(TrafficTracePo::toTrace).collect(Collectors.toList());
//        return cb.and(predicates.toArray(new Predicate[0]));

//
//        Predicate conditionEntryPointId = criteriaBuilder.equal(fromObj.get("configId"), entryPointId.getId());
//        criteriaQuery.where(conditionEntryPointId);
//        criteriaQuery.orderBy(new OrderImpl(fromObj.get("startTime"), false));
//
//        List<EntryPointTracePo> resultPoList = entityManager.createQuery(criteriaQuery)
//                .setFirstResult(pageNum)
//                .setMaxResults(pageSize)
//                .getResultList();
//        List<Trace> traceList = resultPoList.stream().map(obj -> obj.toTrace()).collect(Collectors.toList());

//        final List<TrafficTracePo> pageResult = trafficTracePoRepository.findAll((root, criteriaQuery, cb) -> {


//        },  Sort.by(Sort.Direction.DESC, "id"));

//        return pageResult.getContent().stream().map(TrafficTracePo::toTrace).collect(Collectors.toList());
    }

    @Override
    public void deleteById(EntryPointConfigID entryPointConfigId) {
        apiConfigPoRepository.deleteById(entryPointConfigId.getId());
    }

    @Override
    public Trace byTraceId(TraceId traceId) {
        Optional<TrafficTracePo> trafficTracePo = trafficTracePoRepository.findById(traceId.getId());
        if (trafficTracePo.isEmpty()) {
            return null;
        }
        return trafficTracePo.get().toTrace();
    }

    @Override
    public Trace findBySpanId(Trace traceParam) {
        TrafficTracePo tracePo = trafficTracePoRepository.findBySpanIdOrderByStartTimeAsc(traceParam.getSpanContext().getSpanId());
        if (Objects.isNull(tracePo)) {
            return null;
        }
        return tracePo.toTrace();
    }

    @Override
    public List<Trace> findByTraceId(Trace trace) {
        List<TrafficTracePo> tracePoList = trafficTracePoRepository.findByTraceIdOrderByStartTimeAsc(trace.getSpanContext().getTraceId());
        if (Objects.isNull(tracePoList) || tracePoList.isEmpty()) {
            return new ArrayList<>();
        }
        return tracePoList.stream().map(TrafficTracePo::toTrace).collect(Collectors.toList());
    }

    @Override
    public List<Trace> queryTrafficTraceList(Integer page, Integer size, EntryPointConfig entryPointConfig) {
        int pageSize = size == null ? 10 : size;
        int pageNum = page == null ? 0 : (page - 1) * pageSize;

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TrafficTracePo> criteriaQuery = criteriaBuilder.createQuery(TrafficTracePo.class);
        Root<TrafficTracePo> root = criteriaQuery.from(TrafficTracePo.class);

        HttpPath httpPath = entryPointConfig.getHttpResource().getPath();
        String path = httpPath.getLikePath(httpPath.getPath());
        String method = entryPointConfig.getHttpResource().getMethod().name();

        Predicate conditionPath = criteriaBuilder.like(root.get("path"), path);
        Predicate conditionPathPrefix = criteriaBuilder.like(root.get("path"), path + "?%");
        Predicate conditionMethod = criteriaBuilder.equal(root.get("method"), method);

        Predicate pathClause = criteriaBuilder.or(conditionPath, conditionPathPrefix);
        Predicate conditionWhere = criteriaBuilder.and(pathClause, conditionMethod);

        criteriaQuery.where(conditionWhere);
        criteriaQuery.orderBy(new OrderImpl(root.get("id"), false));

        List<TrafficTracePo> resultPoList = entityManager.createQuery(criteriaQuery)
                .setFirstResult(pageNum)
                .setMaxResults(pageSize)
                .getResultList();
        return resultPoList.stream().map(TrafficTracePo::toTrace).collect(Collectors.toList());
    }

    @Override
    public Long queryTrafficTraceCount(EntryPointConfig entryPointConfig) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<TrafficTracePo> fromObj = criteriaQuery.from(TrafficTracePo.class);
        criteriaQuery.select(criteriaBuilder.count(fromObj));

        HttpPath httpPath = entryPointConfig.getHttpResource().getPath();
        String path = httpPath.getLikePath(httpPath.getPath());
        String method = entryPointConfig.getHttpResource().getMethod().name();

        Predicate conditionPath = criteriaBuilder.like(fromObj.get("path"), path);
        Predicate conditionPathPrefix = criteriaBuilder.like(fromObj.get("path"), path + "?%");
        Predicate conditionMethod = criteriaBuilder.equal(fromObj.get("method"), method);

        Predicate pathClause = criteriaBuilder.or(conditionPath, conditionPathPrefix);
        Predicate conditionWhere = criteriaBuilder.and(pathClause, conditionMethod);
        criteriaQuery.where(conditionWhere);
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    @Override
    public List<EntryPointConfig> findAllEntryPointConfig() {
        return apiConfigPoRepository.findAll()
                .stream().map(EntryPointConfigPO::toEntryPointConfig)
                .collect(Collectors.toList());
    }

    @Override
    public List<EntryPointConfig> findByHttpResource(HttpResource httpResource) {
        if (Objects.isNull(httpResource) || StringUtils.isEmpty(httpResource.getMethod().name()) || StringUtils.isEmpty(httpResource.getPath().getPath())) {
            return null;
        }
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<EntryPointConfigPO> criteriaQuery = criteriaBuilder.createQuery(EntryPointConfigPO.class);
        Root<EntryPointConfigPO> fromObj = criteriaQuery.from(EntryPointConfigPO.class);

        Predicate conditionMethod = criteriaBuilder.equal(fromObj.get("apiMethod"), httpResource.getMethod().name());
        Predicate conditionPath = criteriaBuilder.equal(fromObj.get("apiPath"), httpResource.getPath().getPath());
        Predicate conditionWhere = criteriaBuilder.and(conditionMethod, conditionPath);

        criteriaQuery.where(conditionWhere);
        List<EntryPointConfigPO> resultPoList = entityManager.createQuery(criteriaQuery).getResultList();
        return resultPoList.stream().map(EntryPointConfigPO::toEntryPointConfig).collect(Collectors.toList());
    }

    @Override
    public void deleteAllByDateRange(int day) {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime startTime = today.minusDays(day);
        trafficTracePoRepository.deleteByStartTimeLessThan(startTime);
    }

    @Override
    public void deleteGroupById(EntryPointGroupID entryPointGroupID) {
        apiConfigGroupPoRepository.deleteById(entryPointGroupID.getId());
    }

}
