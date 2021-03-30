package com.oc.hawk.traffic.entrypoint.api.command;

import lombok.Data;

@Data
public class HistoryPageCommand {

    Integer page;
    Integer size;
    Long entryPointId;
    String path;
    String instanceName;
    String spanId;
    
}
