package com.oc.hawk.traffic.port.driven.persistence;

import com.google.common.base.Joiner;
import com.oc.hawk.api.constant.AccountHolder;
import com.oc.hawk.common.utils.AccountHolderUtils;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.*;
import com.oc.hawk.traffic.entrypoint.domain.model.httpresource.HttpMethod;
import com.oc.hawk.traffic.entrypoint.domain.model.httpresource.HttpPath;
import com.oc.hawk.traffic.entrypoint.domain.model.trace.Trace;
import com.oc.hawk.traffic.entrypoint.domain.model.trace.TraceId;
import com.oc.hawk.traffic.port.driven.persistence.po.EntryPointConfigGroupPO;
import com.oc.hawk.traffic.port.driven.persistence.po.EntryPointConfigPO;
import com.oc.hawk.traffic.port.driven.persistence.po.EntryPointGroupManagerPO;
import com.oc.hawk.traffic.port.driven.persistence.po.TrafficTracePo;

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

    List<EntryPointConfigPO> findByGroupIdIn(List<Long> groupIdList);
}

@Component
@RequiredArgsConstructor
@Slf4j
public class JpaApiConfigRepository implements EntryPointConfigRepository {

    private final ApiConfigPoRepository apiConfigPoRepository;
    private final EntryPointConfigGroupPoRepository apiConfigGroupPoRepository;
    private final EntryPointGroupManagerPoRepository apiGroupManagerPoRepository;
    private final TrafficTracePoRepository trafficTracePoRepository;
    
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

        Predicate conditionName = criteriaBuilder.like(fromObj.get("apiName"), '%' + config.getDescription().getName() + '%');
        Predicate conditionDesc = criteriaBuilder.like(fromObj.get("apiDesc"), '%' + config.getDescription().getDesc() + '%');
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
    public List<EntryPointConfigGroup> byIdList(List<EntryPointGroupID> groupIdList) {
        List<Long> groupList = groupIdList.stream().map(obj->obj.getId()).collect(Collectors.toList());
        List<EntryPointConfigGroupPO> apiConfigGroupPoList = apiConfigGroupPoRepository.findByIdIn(groupList);
        List<EntryPointConfigGroup> apiConfigGroupList = apiConfigGroupPoList.stream().map(obj -> obj.toEntryPointConfigGroup()).collect(Collectors.toList());
        return apiConfigGroupList;
    }

    @Override
    public List<EntryPointConfig> byGroupIdList(List<EntryPointGroupID> groupIdList) {
        List<Long> groupList = groupIdList.stream().map(obj->obj.getId()).collect(Collectors.toList());
        List<EntryPointConfigPO> apiConfigPoList = apiConfigPoRepository.findByGroupIdIn(groupList);
        List<EntryPointConfig> apiConfigList = apiConfigPoList.stream().map(obj -> obj.toEntryPointConfig()).collect(Collectors.toList());
        return apiConfigList;
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
    	List<TrafficTracePo> poList = new ArrayList<>();
    	for(Trace trace : traceList) {
    		 TrafficTracePo historyPo = TrafficTracePo.createBy(trace);
    		 poList.add(historyPo);
    	};
    	trafficTracePoRepository.saveAll(poList);
    }

    @Override
    public EntryPointConfig findByPathAndMethod(HttpPath path, HttpMethod method) {
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
    public List<EntryPointConfig> findByMethodAndRestfulPath(HttpMethod method) {
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
        CriteriaQuery<TrafficTracePo> criteriaQuery = criteriaBuilder.createQuery(TrafficTracePo.class);
        Root<TrafficTracePo> fromObj = criteriaQuery.from(TrafficTracePo.class);
        
        Predicate conditionPath = criteriaBuilder.equal(fromObj.get("path"), trace.getHttpResource().getPath().getPath());
        Predicate conditionPathPrefix = criteriaBuilder.like(fromObj.get("path"), trace.getHttpResource().getPath().getPath()+"?%");
        Predicate conditionInstanceName = criteriaBuilder.equal(fromObj.get("dstWorkload"), trace.getDestination().getDstWorkload());
        
        Path<String> dstWorkload = fromObj.get("dstWorkload");
        In<String> inClause = criteriaBuilder.in(dstWorkload);
        visibleInstances.stream().forEach(inClause::value);
        
        Predicate orClause = criteriaBuilder.or(conditionPath, conditionPathPrefix);
        
        if(StringUtils.isEmpty(trace.getHttpResource().getPath().getPath()) && StringUtils.isEmpty(trace.getDestination().getDstWorkload())) {
            criteriaQuery.where(inClause);
        }else if(StringUtils.isEmpty(trace.getDestination().getDstWorkload())){          
            criteriaQuery.where(criteriaBuilder.and(orClause,inClause));
        }else if(StringUtils.isEmpty(trace.getHttpResource().getPath().getPath())) {
            criteriaQuery.where(criteriaBuilder.and(conditionInstanceName,inClause));
        }else {
            Predicate keyClause = criteriaBuilder.or(orClause,conditionInstanceName);
            Predicate whereClause = criteriaBuilder.and(keyClause,inClause);
            criteriaQuery.where(whereClause);
        }
        criteriaQuery.orderBy(new OrderImpl(fromObj.get("startTime"), false));
        List<TrafficTracePo> resultPoList = entityManager.createQuery(criteriaQuery)
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
        Optional<TrafficTracePo> trafficTracePo = trafficTracePoRepository.findById(traceId.getId());
        if (Objects.isNull(trafficTracePo) || trafficTracePo.isEmpty()) {
            return null;
        }
        return trafficTracePo.get().toTrace();
    }

    @Override
    public Trace findBySpanId(Trace traceParam) {
        TrafficTracePo tracePo = trafficTracePoRepository.findBySpanIdOrderByStartTimeAsc(traceParam.getSpanContext().getSpanId());
        if(Objects.isNull(tracePo)) {
            return null;
        }
        return tracePo.toTrace();
    }

    @Override
    public List<Trace> findByTraceId(Trace trace) {
        List<TrafficTracePo> tracePoList = trafficTracePoRepository.findByTraceIdOrderByStartTimeAsc(trace.getSpanContext().getTraceId());
        if(Objects.isNull(tracePoList) || tracePoList.isEmpty()) {
            return new ArrayList<>();
        }
        return tracePoList.stream().map(obj -> obj.toTrace()).collect(Collectors.toList());
    }

    @Override
    public List<Trace> queryTrafficTraceList(Integer page, Integer size, EntryPointConfig entryPointConfig) {
        Integer pageSize = size==null ? 10 : size;
        Integer pageNum = page==null ? 0 : (page-1)*pageSize;
        
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TrafficTracePo> criteriaQuery = criteriaBuilder.createQuery(TrafficTracePo.class);
        Root<TrafficTracePo> fromObj = criteriaQuery.from(TrafficTracePo.class);
        
        String path = entryPointConfig.getHttpResource().getPath().getPath();
        String method = entryPointConfig.getHttpResource().getMethod().name();
        
        Predicate conditionPath = criteriaBuilder.equal(fromObj.get("path"), path);
        Predicate conditionPathPrefix = criteriaBuilder.like(fromObj.get("path"), path+"?%");
        Predicate conditionMethod = criteriaBuilder.equal(fromObj.get("method"), method);
        
        Predicate pathClause = criteriaBuilder.or(conditionPath,conditionPathPrefix);
        Predicate conditionWhere = criteriaBuilder.and(pathClause,conditionMethod);
        
        criteriaQuery.where(conditionWhere);
        criteriaQuery.orderBy(new OrderImpl(fromObj.get("startTime"), false));
        
        List<TrafficTracePo> resultPoList = entityManager.createQuery(criteriaQuery)
                .setFirstResult(pageNum)
                .setMaxResults(pageSize)
                .getResultList();
        List<Trace> traceList = resultPoList.stream().map(obj -> obj.toTrace()).collect(Collectors.toList());
        return traceList;
    }

    @Override
    public Long queryTrafficTraceCount(EntryPointConfig entryPointConfig) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<TrafficTracePo> fromObj = criteriaQuery.from(TrafficTracePo.class);
        criteriaQuery.select(criteriaBuilder.count(fromObj));
        
        String path = entryPointConfig.getHttpResource().getPath().getPath();
        String method = entryPointConfig.getHttpResource().getMethod().name();
        
        Predicate conditionPath = criteriaBuilder.equal(fromObj.get("path"), path);
        Predicate conditionPathPrefix = criteriaBuilder.like(fromObj.get("path"), path+"?%");
        Predicate conditionMethod = criteriaBuilder.equal(fromObj.get("method"), method);
        
        Predicate pathClause = criteriaBuilder.or(conditionPath,conditionPathPrefix);
        Predicate conditionWhere = criteriaBuilder.and(pathClause,conditionMethod);
        criteriaQuery.where(conditionWhere);
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }
    
}
