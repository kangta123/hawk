package com.oc.hawk.container.api.dto;

import lombok.Data;

@Data
public class AddAppRuleDTO {
    private final Integer TOTAL_WEIGHT_VALUE = 100;
    private AddAppRuleWeightDTO weight;

    public void validate() {
        if (weight == null) {
            throw new IllegalArgumentException("添加规则信息不能为空");
        }

        if (weight.getWeight() == null || weight.getWeight().isEmpty()) {
            throw new IllegalArgumentException("权重信息不能为空");
        }
        if (weight.getWeight() != null) {
            int sum = weight.getWeight().values().stream().mapToInt(Integer::parseInt).sum();
            if (sum != TOTAL_WEIGHT_VALUE) {
                throw new IllegalArgumentException("权重值总和不等于100");
            }
        }
    }
}
