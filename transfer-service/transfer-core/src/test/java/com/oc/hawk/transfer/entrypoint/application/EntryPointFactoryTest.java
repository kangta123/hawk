package com.oc.hawk.transfer.entrypoint.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.oc.hawk.test.TestHelper;
import com.oc.hawk.transfer.entrypoint.api.command.CreateEntryPointCommand;
import com.oc.hawk.transfer.entrypoint.api.dto.ImportGroupDTO.ImportApiDTO;
import com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointConfig;
import com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointConfigRepository;
import com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointGroupID;
import com.oc.hawk.transfer.entrypoint.port.driven.persistence.JpaApiConfigRepository;

import java.util.List;
import org.assertj.core.api.Assertions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;

public class EntryPointFactoryTest extends EntryPointBaseTest{
	
	EntryPointConfigFactory entryPointConfigFactory;
	EntryPointConfigRepository entryPointConfigRepository;
	
	@BeforeEach
    public void setup() {
		entryPointConfigFactory = new EntryPointConfigFactory(entryPointConfigRepository);
    }
	
	@Test
	@DisplayName("entry point config created by factory cannot be null")
	void create_entryPointConfigNotNull() {
		CreateEntryPointCommand command = getCreateEntryPointCommand();
		EntryPointConfig entryPointConfig = entryPointConfigFactory.create(command);
		
		Assertions.assertThat(entryPointConfig).isNotNull();
		Assertions.assertThat(entryPointConfig.getGroupId()).isNotNull();
	}
	
	@Test
    void createGroupInvisibility() {
		List<EntryPointGroupID> groupIdList = entryPointConfigFactory.createGroupInvisibility(List.of(along()));
        Assertions.assertThat(groupIdList).isNotEmpty();
    }
	
	@Test
	void create_importApiList() {
		List<EntryPointConfig> entryPointConfigList = entryPointConfigFactory.create(List.of(getImportApiDTO()));
		Assertions.assertThat(entryPointConfigList).isNotEmpty();
	}
	
	CreateEntryPointCommand getCreateEntryPointCommand() {
		CreateEntryPointCommand command = TestHelper.newInstance(CreateEntryPointCommand.class);
		command.setGroupId(along());
		command.setApp(str());
		command.setDesc(str());
		command.setMethod("POST");
		command.setPath(str());
		command.setProjectId(along());
        return command;
	}
	
	ImportApiDTO getImportApiDTO() {
		ImportApiDTO importApiDTO = TestHelper.newInstance(ImportApiDTO.class);
		importApiDTO.setName(str());
		importApiDTO.setMethod("POST");
		importApiDTO.setUrl(str());
		importApiDTO.setDescription(str());
		return importApiDTO;
	}
	
}
