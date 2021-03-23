package com.oc.hawk.traffic.entrypoint.api.dto;

import lombok.Data;

import java.util.List;

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
