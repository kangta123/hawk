package com.co.hawk.transfer.entrypoint.port.driven.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.co.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointConfig;
import com.co.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointConfigGroup;
import com.co.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointConfigID;
import com.co.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointConfigRepository;
import com.co.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointGroupID;
import com.co.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointGroupVisibility;
import com.co.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointPath;
import com.co.hawk.transfer.entrypoint.port.driven.persistence.po.EntryPointConfigGroupPO;
import com.co.hawk.transfer.entrypoint.port.driven.persistence.po.EntryPointConfigPO;
import com.co.hawk.transfer.entrypoint.port.driven.persistence.po.EntryPointGroupManagerPO;
import com.google.common.base.Joiner;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
interface ApiConfigPoRepository extends JpaRepositoryImplementation<EntryPointConfigPO, Long> {
	
	List<EntryPointConfigPO> findByGroupId(Long groupId);
	
	Optional<EntryPointConfigPO> findById(Long id);
	
	//path前后要加%xxx%
	List<EntryPointConfigPO> findByApiPathLike(String path);
	
	List<EntryPointConfigPO> findByGroupIdNotIn(List<Long> groupIdList);
}

@Component
@RequiredArgsConstructor
@Slf4j
public class JpaApiConfigRepository implements EntryPointConfigRepository{
	
	private final ApiConfigPoRepository apiConfigPoRepository;
	private final EntryPointConfigGroupPoRepository apiConfigGroupPoRepository;
	private final EntryPointGroupManagerPoRepository apiGroupManagerPoRepository;
	
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
		EntryPointConfigPO apiPO = apiConfigPoRepository.findById(apiConfigId.getId()).get();
		return apiPO.toEntryPointConfig();
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
	public List<EntryPointConfig> byKey(EntryPointConfig config) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<EntryPointConfigPO> criteriaQuery = criteriaBuilder.createQuery(EntryPointConfigPO.class);
		Root<EntryPointConfigPO> fromObj = criteriaQuery.from(EntryPointConfigPO.class);
		
		Predicate conditionName = criteriaBuilder.like(fromObj.get("apiName"),'%' + config.getDesign().getName() + '%');
		Predicate conditionDesc = criteriaBuilder.like(fromObj.get("apiDesc"),'%' + config.getDesign().getDesc() + '%');
		Predicate conditionPath = criteriaBuilder.like(fromObj.get("apiPath"),'%' + config.getHttpResource().getPath().getPath() + '%');
		
		Predicate conditionWhere = criteriaBuilder.or(conditionName,conditionPath,conditionDesc);
		criteriaQuery.where(conditionWhere);
		List<EntryPointConfigPO> resultPOList = entityManager.createQuery(criteriaQuery).getResultList();
		
		//List<ApiConfigPO> poList = apiConfigPoRepository.findByApiPathLike("%"+apiPath.getPath()+"%");
		
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
	public List<EntryPointConfigGroup> findGroupsByUserId(Long userId) {
		EntryPointGroupManagerPO managerPO = apiGroupManagerPoRepository.findByUserId(userId);
		if(Objects.isNull(managerPO)) {
			return null;
		}
		
		String groupids = managerPO.getGroupids();
		String[] groupList = groupids.split(",");
		
		List<EntryPointConfigGroup> apiGroupList = new ArrayList<EntryPointConfigGroup>();
		for(String groupIdStr : groupList) {
			Long groupId = Long.parseLong(groupIdStr);
			EntryPointConfigGroupPO apiConfigGroupPO = apiConfigGroupPoRepository.findById(groupId).get();
			apiGroupList.add(apiConfigGroupPO.toEntryPointConfigGroup());
		}
		return apiGroupList;
	}
	
	@Override
	public void update(Long userId, EntryPointGroupVisibility visibility) {
		EntryPointGroupManagerPO managerPO = apiGroupManagerPoRepository.findByUserId(userId);
		if(Objects.isNull(managerPO)) {
			managerPO = new EntryPointGroupManagerPO();
			managerPO.setUserId(userId);
		}
		List<EntryPointGroupID> grougIDList = visibility.getGroupids();
		List<Long> groupIdList = grougIDList.stream().map(obj -> obj.getId()).collect(Collectors.toList());
		String groupids = Joiner.on(",").join(groupIdList);
		managerPO.setGroupids(groupids);
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
		List<EntryPointConfigPO> apiConfigPOList = apiConfigPoRepository.findByGroupIdNotIn(groupIdList);
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
	
}