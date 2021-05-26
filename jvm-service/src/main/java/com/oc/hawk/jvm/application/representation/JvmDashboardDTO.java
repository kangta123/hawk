package com.oc.hawk.jvm.application.representation;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author kangta123
 */
@Data
public class JvmDashboardDTO {
    private  List<JvmGcDTO> gc;
    private  List<JvmThreadDTO> thread;
    private  Map<String, List<JvmMemoryDTO>> memory;
}
