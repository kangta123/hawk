package com.oc.hawk.transfer.entrypoint.api.command;

import java.util.List;
import lombok.Data;

@Data
public class CreateEntryPointHistoryCommand {
	
	private Long start;
	private Long end;
	private String requestBody;
	private String responseBody; 
	private List<List<String>> requestHeaders;
	private List<List<String>> responseHeaders;
	
}
