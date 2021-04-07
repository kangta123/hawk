package com.oc.hawk.traffic.port.driven.persistence;

import com.google.common.base.Joiner;
import com.oc.hawk.api.constant.AccountHolder;
import com.oc.hawk.common.utils.AccountHolderUtils;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.*;
import com.oc.hawk.traffic.entrypoint.domain.model.trace.Trace;
import com.oc.hawk.traffic.entrypoint.domain.model.trace.TraceId;
import com.oc.hawk.traffic.port.driven.persistence.po.EntryPointConfigGroupPO;
import com.oc.hawk.traffic.port.driven.persistence.po.EntryPointConfigPO;
import com.oc.hawk.traffic.port.driven.persistence.po.EntryPointGroupManagerPO;
import com.oc.hawk.traffic.port.driven.persistence.po.EntryPointTracePo;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.hibernate.query.criteria.internal.OrderImpl;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import javax.persistence.criteria.CriteriaBuilder.In;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Repository
interface ApiConfigPoRepository extends JpaRepositoryImplementation<EntryPointConfigPO, Long> {

    List<EntryPointConfigPO> findByGroupId(Long groupId);

    Optional<EntryPointConfigPO> findById(Long id);

    //path前后要加%xxx%
    List<EntryPointConfigPO> findByApiPathLike(String path);

    List<EntryPointConfigPO> findByGroupIdIn(List<Long> groupIdList);
}

@Component
@RequiredArgsConstructor
@Slf4j
public class JpaApiConfigRepository implements EntryPointConfigRepository {

    private final ApiConfigPoRepository apiConfigPoRepository;
    private final EntryPointConfigGroupPoRepository apiConfigGroupPoRepository;
    private final EntryPointGroupManagerPoRepository apiGroupManagerPoRepository;
    private final EntryPointTracePoRepository entryPointTracePoRepository;
    
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public EntryPointConfigID save(EntryPointConfig config) {
        EntryPointConfigPO apiConfigPo = EntryPointConfigPO.createBy(config);
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
    public List<EntryPointConfig> byGroupId(Long groupId) {
        List<EntryPointConfigPO> poList = apiConfigPoRepository.findByGroupId(groupId);

        List<EntryPointConfig> apiList = poList.stream().map(po -> po.toEntryPointConfig()).collect(Collectors.toList());
        return apiList;
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
            return new ArrayList<EntryPointConfig>();
        }

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<EntryPointConfigPO> criteriaQuery = criteriaBuilder.createQuery(EntryPointConfigPO.class);
        Root<EntryPointConfigPO> fromObj = criteriaQuery.from(EntryPointConfigPO.class);

        Path<Long> path = fromObj.get("groupId");
        In<Long> inClause = criteriaBuilder.in(path);
        groupList.stream().map(obj -> obj.getGroupId().getId()).collect(Collectors.toList()).forEach(inClause::value);

        Predicate conditionName = criteriaBuilder.like(fromObj.get("apiName"), '%' + config.getDesign().getName() + '%');
        Predicate conditionDesc = criteriaBuilder.like(fromObj.get("apiDesc"), '%' + config.getDesign().getDesc() + '%');
        Predicate conditionPath = criteriaBuilder.like(fromObj.get("apiPath"), '%' + config.getHttpResource().getPath().getPath() + '%');

        Predicate conditionWhere = criteriaBuilder.and(inClause, criteriaBuilder.or(conditionName, conditionDesc, conditionPath));

        criteriaQuery.where(conditionWhere);
        List<EntryPointConfigPO> resultPoList = entityManager.createQuery(criteriaQuery).getResultList();

        List<EntryPointConfig> apiList = resultPoList.stream().map(po -> po.toEntryPointConfig()).collect(Collectors.toList());
        return apiList;
    }

    @Override
    public List<EntryPointConfigGroup> findAllGroup() {
        List<EntryPointConfigGroupPO> poList = apiConfigGroupPoRepository.findAll();

        List<EntryPointConfigGroup> groupList = poList.stream().map(po -> po.toEntryPointConfigGroup()).collect(Collectors.toList());

        return groupList;
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
        if(Objects.nonNull(holder)) {
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
        if(Objects.nonNull(accountHolder)) {
            managerPo = apiGroupManagerPoRepository.findByUserId(accountHolder.getId());
        }
        if (Objects.isNull(managerPo)) {
            managerPo = new EntryPointGroupManagerPO();
            managerPo.setUserId(accountHolder.getId());
        }
        List<Long> groupIdList = entryPointGroupIdList.stream().map(obj -> obj.getId()).collect(Collectors.toList());
        String groupids = Joiner.on(",").join(groupIdList);
        managerPo.setGroupids(groupids);
        managerPo.setCreateTime(LocalDateTime.now());
        managerPo.setUpdateTime(LocalDateTime.now());
        apiGroupManagerPoRepository.save(managerPo);
    }

    @Override
    public List<EntryPointConfigGroup> byIdList(List<Long> groupIdList) {
        List<EntryPointConfigGroupPO> apiConfigGroupPoList = apiConfigGroupPoRepository.findByIdIn(groupIdList);
        List<EntryPointConfigGroup> apiConfigGroupList = apiConfigGroupPoList.stream().map(obj -> obj.toEntryPointConfigGroup()).collect(Collectors.toList());
        return apiConfigGroupList;
    }

    @Override
    public List<EntryPointConfig> byGroupIdList(List<Long> groupIdList) {
        List<EntryPointConfigPO> apiConfigPoList = apiConfigPoRepository.findByGroupIdIn(groupIdList);
        List<EntryPointConfig> apiConfigList = apiConfigPoList.stream().map(obj -> obj.toEntryPointConfig()).collect(Collectors.toList());
        return apiConfigList;
    }

    @Override
    public void updateList(Long userId, List<EntryPointConfigGroup> groupList) {
        EntryPointGroupManagerPO managerPo = apiGroupManagerPoRepository.findByUserId(userId);
        if (Objects.isNull(managerPo)) {
            managerPo = new EntryPointGroupManagerPO();
            managerPo.setUserId(userId);
        }
        String groupids = Joiner.on(",").join(groupList);
        managerPo.setGroupids(groupids);
        apiGroupManagerPoRepository.save(managerPo);
    }

    @Override
    public void batchSave(EntryPointGroupID entryPointConfigGroupId, List<EntryPointConfig> apiConfigList) {
        try {
            for (EntryPointConfig apiConfig : apiConfigList) {
                EntryPointConfigPO apiConfigPo = EntryPointConfigPO.createBy(apiConfig);
                apiConfigPo.setGroupId(entryPointConfigGroupId.getId());
                apiConfigPoRepository.save(apiConfigPo);
            }
        } catch (Exception e) {
            log.error("batchSaveException:{}", e.getMessage(), e);
        }
    }

    @Override
    public void saveTrace(List<Trace> traceList) {
    	List<EntryPointTracePo> poList = new ArrayList<>();
    	for(Trace trace : traceList) {
    		 EntryPointTracePo historyPo = EntryPointTracePo.createBy(trace);
    		 poList.add(historyPo);
    	};
    	entryPointTracePoRepository.saveAll(poList);
    }

    @Override
    public EntryPointConfig findByPathAndMethod(EntryPointPath path, EntryPointMethod method) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<EntryPointConfigPO> criteriaQuery = criteriaBuilder.createQuery(EntryPointConfigPO.class);
        Root<EntryPointConfigPO> fromObj = criteriaQuery.from(EntryPointConfigPO.class);

        Predicate conditionPath = criteriaBuilder.equal(fromObj.get("apiPath"), path.getPath());
        Predicate conditionPathPrefix = criteriaBuilder.equal(fromObj.get("apiPath"), path.getPath() + "/");
        
        Predicate conditionMethod = criteriaBuilder.equal(fromObj.get("apiMethod"), method.name());
        Predicate orClause = criteriaBuilder.or(conditionPath, conditionPathPrefix);
        
        Predicate conditionWhere = criteriaBuilder.and(conditionMethod, orClause);
        criteriaQuery.where(conditionWhere);
        
        List<EntryPointConfigPO> resultPoList = entityManager.createQuery(criteriaQuery).getResultList();
        if (Objects.isNull(resultPoList) || resultPoList.isEmpty()) {
            return null;
        }
        return resultPoList.get(0).toEntryPointConfig();
    }

    @Override
    public List<EntryPointConfig> findByMethodAndRestfulPath(EntryPointMethod method) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<EntryPointConfigPO> criteriaQuery = criteriaBuilder.createQuery(EntryPointConfigPO.class);
        Root<EntryPointConfigPO> fromObj = criteriaQuery.from(EntryPointConfigPO.class);
        Predicate conditionPath = criteriaBuilder.like(fromObj.get("apiPath"), "%{%}%");
        Predicate conditionMethod = criteriaBuilder.equal(fromObj.get("apiMethod"), method.name());
        
        Predicate conditionWhere = criteriaBuilder.and(conditionMethod, conditionPath);
        criteriaQuery.where(conditionWhere);
        
        List<EntryPointConfigPO> resultPoList = entityManager.createQuery(criteriaQuery).getResultList();
        List<EntryPointConfig> entryPointConfigList = resultPoList.stream().map(obj -> obj.toEntryPointConfig()).collect(Collectors.toList());
        return entryPointConfigList;
    }

    @Override
    public List<Trace> queryTraceInfoList(Integer page,Integer size,Trace trace,List<String> visibleInstances) {
        Integer pageSize = size==null ? 10 : size;
        Integer pageNum = page==null ? 0 : (page-1)*pageSize;
        //多查一条
        pageSize+=1;
        
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<EntryPointTracePo> criteriaQuery = criteriaBuilder.createQuery(EntryPointTracePo.class);
        Root<EntryPointTracePo> fromObj = criteriaQuery.from(EntryPointTracePo.class);
        
        Predicate conditionPath = criteriaBuilder.equal(fromObj.get("path"), trace.getPath());
        Predicate conditionPathPrefix = criteriaBuilder.like(fromObj.get("path"), trace.getPath()+"?%");
        Predicate conditionInstanceName = criteriaBuilder.equal(fromObj.get("dstWorkload"), trace.getDstWorkload());
        
        Path<String> dstWorkload = fromObj.get("dstWorkload");
        In<String> inClause = criteriaBuilder.in(dstWorkload);
        visibleInstances.stream().forEach(inClause::value);
        
        Predicate orClause = criteriaBuilder.or(conditionPath, conditionPathPrefix);
        
        if(StringUtils.isBlank(trace.getPath()) && StringUtils.isBlank(trace.getDstWorkload())) {
            criteriaQuery.where(inClause);
            criteriaQuery.orderBy(new OrderImpl(fromObj.get("startTime"), false));
        }else if(StringUtils.isBlank(trace.getDstWorkload())){          
            criteriaQuery.where(criteriaBuilder.and(orClause,inClause));
            criteriaQuery.orderBy(new OrderImpl(fromObj.get("startTime"), false));
        }else if(StringUtils.isBlank(trace.getPath())) {
            criteriaQuery.where(criteriaBuilder.and(conditionInstanceName,inClause));
            criteriaQuery.orderBy(new OrderImpl(fromObj.get("startTime"), false));
        }else {
            Predicate whereClause = criteriaBuilder.and(orClause,conditionInstanceName,inClause);
            criteriaQuery.where(whereClause);
        }
        List<EntryPointTracePo> resultPoList = entityManager.createQuery(criteriaQuery)
                .setFirstResult(pageNum)
                .setMaxResults(pageSize)
                .getResultList();
        List<Trace> traceList = resultPoList.stream().map(obj -> obj.toTrace()).collect(Collectors.toList());
        return traceList;
    }
    
    @Override
    public void deleteById(EntryPointConfigID entryPointConfigId) {
        apiConfigPoRepository.deleteById(entryPointConfigId.getId());
    }

    @Override
    public Trace byTraceId(TraceId traceId) {
        Optional<EntryPointTracePo> entryPointTracePo = entryPointTracePoRepository.findById(traceId.getId());
        if (Objects.isNull(entryPointTracePo) || entryPointTracePo.isEmpty()) {
            return null;
        }
        return entryPointTracePo.get().toTrace();
    }

    @Override
    public Trace findBySpanId(Trace traceParam) {
        EntryPointTracePo tracePo = entryPointTracePoRepository.findBySpanIdOrderByStartTimeAsc(traceParam.getSpanId());
        if(Objects.isNull(tracePo)) {
            return null;
        }
        return tracePo.toTrace();
    }

    @Override
    public List<Trace> findByTraceId(Trace trace) {
        List<EntryPointTracePo> tracePoList = entryPointTracePoRepository.findByTraceIdOrderByStartTimeAsc(trace.getTraceId());
        if(Objects.isNull(tracePoList) || tracePoList.isEmpty()) {
            return new ArrayList<>();
        }
        return tracePoList.stream().map(obj -> obj.toTrace()).collect(Collectors.toList());
    }

    @Override
    public List<Trace> queryApiHistoryList(Integer page, Integer size, EntryPointConfigID entryPointId) {
        Integer pageSize = size==null ? 10 : size;
        Integer pageNum = page==null ? 0 : (page-1)*pageSize;
        
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<EntryPointTracePo> criteriaQuery = criteriaBuilder.createQuery(EntryPointTracePo.class);
        Root<EntryPointTracePo> fromObj = criteriaQuery.from(EntryPointTracePo.class);
        
        Predicate conditionEntryPointId = criteriaBuilder.equal(fromObj.get("configId"), entryPointId.getId());
        criteriaQuery.where(conditionEntryPointId);
        criteriaQuery.orderBy(new OrderImpl(fromObj.get("startTime"), false));
        
        List<EntryPointTracePo> resultPoList = entityManager.createQuery(criteriaQuery)
                .setFirstResult(pageNum)
                .setMaxResults(pageSize)
                .getResultList();
        List<Trace> traceList = resultPoList.stream().map(obj -> obj.toTrace()).collect(Collectors.toList());
        return traceList;
    }

    @Override
    public Long queryApiHistoryCount(EntryPointConfigID entryPointId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<EntryPointTracePo> fromObj = criteriaQuery.from(EntryPointTracePo.class);
        criteriaQuery.select(criteriaBuilder.count(fromObj));
        
        Predicate conditionEntryPointId = criteriaBuilder.equal(fromObj.get("configId"), entryPointId.getId());
        criteriaQuery.where(conditionEntryPointId);
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

}
