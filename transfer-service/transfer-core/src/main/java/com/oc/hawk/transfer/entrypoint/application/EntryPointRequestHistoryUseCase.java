package com.oc.hawk.transfer.entrypoint.application;

import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.oc.hawk.transfer.entrypoint.api.command.UploadTraceInfoCommand;
import com.oc.hawk.transfer.entrypoint.application.representation.EntryPointConfigRepresentation;
import com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointConfigRepository;
import com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointHistory;
import com.oc.hawk.transfer.entrypoint.domain.service.excutor.EntryPointExcutor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class EntryPointRequestHistoryUseCase {
	
	private final EntryPointConfigFactory entryPointConfigFactory;
	private final EntryPointConfigRepository entryPointConfigRepository;
	
	@Transactional(rollbackFor = Exception.class)
	public void createRequestHistory(List<UploadTraceInfoCommand> commandList) {
		for(UploadTraceInfoCommand command : commandList) {
			EntryPointHistory history = entryPointConfigFactory.createHistory(command);
			entryPointConfigRepository.saveHistoy(history);
		}
	}
	
}
