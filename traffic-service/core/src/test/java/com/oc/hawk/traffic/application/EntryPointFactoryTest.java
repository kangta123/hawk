package com.oc.hawk.traffic.application;

import com.oc.hawk.test.TestHelper;
import com.oc.hawk.traffic.application.entrypoint.EntryPointConfigFactory;
import com.oc.hawk.traffic.entrypoint.api.command.CreateEntryPointCommand;
import com.oc.hawk.traffic.entrypoint.api.dto.ImportGroupDTO.ImportApiDTO;
import com.oc.hawk.traffic.entrypoint.domain.config.TrafficTraceHeaderFilterConfig;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointConfig;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointConfigGroup;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointConfigRepository;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointGroupID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

public class EntryPointFactoryTest extends TrafficBaseTest {

    EntryPointConfigFactory entryPointConfigFactory;
    TrafficTraceHeaderFilterConfig trafficTraceHeaderFilterConfig;
    @BeforeEach
    public void setup() {
        entryPointConfigFactory = new EntryPointConfigFactory(trafficTraceHeaderFilterConfig);
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
        EntryPointConfigGroup group = EntryPointConfigGroup.builder().groupId(new EntryPointGroupID(1L)).build();
        List<EntryPointConfig> entryPointConfigList = entryPointConfigFactory.create(group,List.of(getImportApiDTO()));
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
