package com.oc.hawk.traffic.application.entrypoint;

import com.oc.hawk.traffic.entrypoint.api.command.UploadTraceInfoCommand;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointConfigRepository;
import com.oc.hawk.traffic.entrypoint.domain.model.trace.Trace;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class EntryPointRequestHistoryUseCase {

    private final EntryPointConfigFactory entryPointConfigFactory;
    private final EntryPointConfigRepository entryPointConfigRepository;

    @Transactional(rollbackFor = Exception.class)
    public void createRequestHistory(List<UploadTraceInfoCommand> commandList) {
        for (UploadTraceInfoCommand command : commandList) {
            Trace history = entryPointConfigFactory.createHistory(command);
            entryPointConfigRepository.saveHistoy(history);
        }
    }

}
