package com.co.hawk.transfer.entrypoint.api.dto;

import java.util.Map;
import lombok.Data;

@Data
public class ExecuteResponseDTO {
	
	private String responseCode;
	
	private Long responseTime;
	
	private Map<String,String> responseHeader; 
	
	private String responseBody;
	
}
