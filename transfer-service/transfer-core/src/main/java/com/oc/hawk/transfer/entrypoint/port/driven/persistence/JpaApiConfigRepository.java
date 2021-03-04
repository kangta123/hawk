package com.oc.hawk.transfer.entrypoint.port.driven.persistence;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.google.common.base.Joiner;
import com.oc.hawk.api.constant.AccountHolder;
import com.oc.hawk.common.utils.AccountHolderUtils;
import com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointConfig;
import com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointConfigGroup;
import com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointConfigID;
import com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointConfigRepository;
import com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointGroupID;
import com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointGroupVisibility;
import com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointHistory;
import com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointMethod;
import com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointPath;
import com.oc.hawk.transfer.entrypoint.port.driven.persistence.po.EntryPointConfigGroupPO;
import com.oc.hawk.transfer.entrypoint.port.driven.persistence.po.EntryPointConfigPO;
import com.oc.hawk.transfer.entrypoint.port.driven.persistence.po.EntryPointGroupManagerPO;
import com.oc.hawk.transfer.entrypoint.port.driven.persistence.po.EntryPointHistoryManagerPo;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
public class JpaApiConfigRepository implements EntryPointConfigRepository{
	
	private final ApiConfigPoRepository apiConfigPoRepository;
	private final EntryPointConfigGroupPoRepository apiConfigGroupPoRepository;
	private final EntryPointGroupManagerPoRepository apiGroupManagerPoRepository;
	private final EntryPointHistoryManagerPoRepository entryPointHistoryManagerPoRepository;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public EntryPointConfigID save(EntryPointConfig config) {
		EntryPointConfigPO apiConfigPO = EntryPointConfigPO.createBy(config);
		apiConfigPoRepository.save(apiConfigPO);
		return new EntryPointConfigID(apiConfigPO.getId());
	}
	
	@Override
	public EntryPointConfig byId(EntryPointConfigID apiConfigId) {
		Optional<EntryPointConfigPO> apiPO = apiConfigPoRepository.findById(apiConfigId.getId());
		if(Objects.isNull(apiPO) || apiPO.isEmpty()) {
			return null;
		}
		return apiPO.get().toEntryPointConfig();
	}
	
	@Override
	public List<EntryPointConfig> byGroupId(Long groupId) {
		List<EntryPointConfigPO> poList = apiConfigPoRepository.findByGroupId(groupId);
		
		List<EntryPointConfig> apiList = poList.stream().map(po -> po.toEntryPointConfig()).collect(Collectors.toList());
		return apiList;
	}
	
	@Override
	public EntryPointGroupID save(EntryPointConfigGroup group) {
		EntryPointConfigGroupPO groupPO = EntryPointConfigGroupPO.createBy(group);
		apiConfigGroupPoRepository.save(groupPO);
		return new EntryPointGroupID(groupPO.getId());
	}

	@Override
	public List<EntryPointConfig> byKey(EntryPointConfig config,List<EntryPointConfigGroup> groupList) {
		if(Objects.isNull(groupList) || groupList.isEmpty()) {
			return new ArrayList<EntryPointConfig>();
		}
		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<EntryPointConfigPO> criteriaQuery = criteriaBuilder.createQuery(EntryPointConfigPO.class);
		Root<EntryPointConfigPO> fromObj = criteriaQuery.from(EntryPointConfigPO.class);
		
		Path<Long> path = fromObj.get("groupId");
		In<Long> inClause = criteriaBuilder.in(path);
		groupList.stream().map(obj -> obj.getGroupId().getId()).collect(Collectors.toList()).forEach(inClause::value);
		
		Predicate conditionName = criteriaBuilder.like(fromObj.get("apiName"),'%' + config.getDesign().getName() + '%');
		Predicate conditionDesc = criteriaBuilder.like(fromObj.get("apiDesc"),'%' + config.getDesign().getDesc() + '%');
		Predicate conditionPath = criteriaBuilder.like(fromObj.get("apiPath"),'%' + config.getHttpResource().getPath().getPath() + '%');
		
		Predicate conditionWhere = criteriaBuilder.and(inClause,criteriaBuilder.or(conditionName,conditionDesc,conditionPath));
		
		criteriaQuery.where(conditionWhere);
		List<EntryPointConfigPO> resultPOList = entityManager.createQuery(criteriaQuery).getResultList();
		
		List<EntryPointConfig> apiList = resultPOList.stream().map(po -> po.toEntryPointConfig()).collect(Collectors.toList());
		return apiList;
	}
	
	@Override
	public List<EntryPointConfigGroup> findAllGroup() {
		List<EntryPointConfigGroupPO> poList = apiConfigGroupPoRepository.findAll();
		
		List<EntryPointConfigGroup> groupList = poList.stream().map(po -> po.toEntryPointConfigGroup()).collect(Collectors.toList());
		
		return groupList;
	}

	/**
	@Override
	public EntryPointGroupVisibility byGroupManagerUserId(Long userId){
		//通过userid获取groupmanager
		ApiGroupManagerPO managerPO = apiGroupManagerPoRepository.findByUserId(userId);
		String groupids = managerPO.getGroupids();
		
		EntryPointGroupVisibility groupManager = EntryPointGroupVisibility.create(userId, groupids);
		return groupManager;
	}
	*/
	
	@Override
	public EntryPointConfigGroup byId(EntryPointGroupID apiConfigGroupId) {
		EntryPointConfigGroupPO groupPO = apiConfigGroupPoRepository.findById(apiConfigGroupId.getId()).get();
		return groupPO.toEntryPointConfigGroup();
	}

	@Override
	public List<EntryPointConfigGroup> findGroups() {
		AccountHolder holder = AccountHolderUtils.getAccountHolder();
		EntryPointGroupManagerPO managerPO = apiGroupManagerPoRepository.findByUserId(holder.getId());
		List<EntryPointConfigGroup> allGroupList = findAllGroup();
		if(Objects.isNull(managerPO) || StringUtils.isBlank(managerPO.getGroupids())) {
			return allGroupList;
		}
		
		String groupids = managerPO.getGroupids();
		String[] groupList = groupids.split(",");
		
		List<EntryPointConfigGroup> apiGroupList = new ArrayList<EntryPointConfigGroup>();
		for(String groupIdStr : groupList) {
			Long groupId = Long.parseLong(groupIdStr);
			EntryPointConfigGroupPO apiConfigGroupPO = apiConfigGroupPoRepository.findById(groupId).get();
			apiGroupList.add(apiConfigGroupPO.toEntryPointConfigGroup());
		}
		allGroupList.removeAll(apiGroupList);
		return allGroupList;
	}
	
	@Override
	public void update(List<EntryPointGroupID> entryPointGroupIdList) {
		AccountHolder accountHolder = AccountHolderUtils.getAccountHolder();
		EntryPointGroupManagerPO managerPO = apiGroupManagerPoRepository.findByUserId(accountHolder.getId());
		if(Objects.isNull(managerPO)) {
			managerPO = new EntryPointGroupManagerPO();
			managerPO.setUserId(1L);
		}
		List<Long> groupIdList = entryPointGroupIdList.stream().map(obj -> obj.getId()).collect(Collectors.toList());
		String groupids = Joiner.on(",").join(groupIdList);
		managerPO.setGroupids(groupids);
		managerPO.setCreateTime(new Timestamp(new Date().getTime()));
		managerPO.setUpdateTime(new Timestamp(new Date().getTime()));
		apiGroupManagerPoRepository.save(managerPO);
	}

	@Override
	public List<EntryPointConfigGroup> byIdList(List<Long> groupIdList) {
		List<EntryPointConfigGroupPO> apiConfigGroupPOList = apiConfigGroupPoRepository.findByIdIn(groupIdList);
		List<EntryPointConfigGroup> apiConfigGroupList = apiConfigGroupPOList.stream().map(obj -> obj.toEntryPointConfigGroup()).collect(Collectors.toList());
		return apiConfigGroupList;
	}

	@Override
	public List<EntryPointConfig> byGroupIdList(List<Long> groupIdList) {
		List<EntryPointConfigPO> apiConfigPOList = apiConfigPoRepository.findByGroupIdIn(groupIdList);
		List<EntryPointConfig> apiConfigList = apiConfigPOList.stream().map(obj -> obj.toEntryPointConfig()).collect(Collectors.toList());
		return apiConfigList;
	}

	@Override
	public void updateList(Long userId, List<EntryPointConfigGroup> groupList) {
		EntryPointGroupManagerPO managerPO = apiGroupManagerPoRepository.findByUserId(userId);
		if(Objects.isNull(managerPO)) {
			managerPO = new EntryPointGroupManagerPO();
			managerPO.setUserId(userId);
		}
		String groupids = Joiner.on(",").join(groupList);
		managerPO.setGroupids(groupids);
		apiGroupManagerPoRepository.save(managerPO);
	}
	
	@Override
	public void batchSave(EntryPointGroupID entryPointConfigGroupID,List<EntryPointConfig> apiConfigList) {
		try {
			for(EntryPointConfig apiConfig : apiConfigList) {
				EntryPointConfigPO apiConfigPO = EntryPointConfigPO.createBy(apiConfig);
				apiConfigPO.setGroupId(entryPointConfigGroupID.getId());
				apiConfigPoRepository.save(apiConfigPO);
			}
		}catch(Exception e) {
			log.error("batchSaveException:{}",e.getMessage(),e);
		}
	}
	
	@Override
	public void saveHistoy(EntryPointHistory history) {
		EntryPointHistoryManagerPo historyPo = EntryPointHistoryManagerPo.createBy(history);
		entryPointHistoryManagerPoRepository.save(historyPo);
	}
	
	@Override
	public EntryPointConfig findByPathAndMethod(EntryPointPath path, EntryPointMethod method) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<EntryPointConfigPO> criteriaQuery = criteriaBuilder.createQuery(EntryPointConfigPO.class);
		Root<EntryPointConfigPO> fromObj = criteriaQuery.from(EntryPointConfigPO.class);
		
		Predicate conditionPath = criteriaBuilder.equal(fromObj.get("apiPath"),path.getPath());
		Predicate conditionPathPrefix = criteriaBuilder.equal(fromObj.get("apiPath"),path.getPath()+"/");
		
		Predicate conditionMethod = criteriaBuilder.equal(fromObj.get("apiMethod"),method.name());
		Predicate orClause = criteriaBuilder.or(conditionPath,conditionPathPrefix);
		
		Predicate conditionWhere = criteriaBuilder.and(conditionMethod,orClause);
		criteriaQuery.where(conditionWhere);
		
		EntryPointConfigPO resultPo = entityManager.createQuery(criteriaQuery).getSingleResult();
		if(Objects.isNull(resultPo)) {
			return null;
		}
		return resultPo.toEntryPointConfig();
	}

	@Override
	public List<EntryPointConfig> findByMethodAndRestfulPath(EntryPointMethod method) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<EntryPointConfigPO> criteriaQuery = criteriaBuilder.createQuery(EntryPointConfigPO.class);
		Root<EntryPointConfigPO> fromObj = criteriaQuery.from(EntryPointConfigPO.class);
		Predicate conditionPath = criteriaBuilder.like(fromObj.get("apiPath"),"%{%}%");
		Predicate conditionMethod = criteriaBuilder.equal(fromObj.get("apiMethod"),method.name());
		
		Predicate conditionWhere = criteriaBuilder.and(conditionMethod,conditionPath);
		criteriaQuery.where(conditionWhere);
		
		List<EntryPointConfigPO> resultPoList = entityManager.createQuery(criteriaQuery).getResultList();
		List<EntryPointConfig> entryPointConfigList = resultPoList.stream().map(obj-> obj.toEntryPointConfig()).collect(Collectors.toList());
		return entryPointConfigList;
	}
	
}