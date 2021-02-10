package com.co.hawk.transfer.entrypoint.api.dto;

import java.util.List;

import lombok.Data;

@Data
public class ImportGroupDTO {
	
	private String name;
	private List<ImportApiDTO> requests;
	
	@Data
	public static class ImportApiDTO {
		
		private String url;
		
		private String method;
		
		private String name;
		
		private String description;
		
	}
	
}
