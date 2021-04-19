package com.oc.hawk.container.runtime.port.driving.rest;

import com.oc.hawk.common.spring.mvc.BooleanWrapper;
import com.oc.hawk.container.api.dto.InstanceConfigDTO;
import com.oc.hawk.container.domain.model.runtime.config.InstanceId;
import com.oc.hawk.container.runtime.application.instance.InstanceConfigUseCase;
import com.oc.hawk.container.runtime.application.instance.InstanceExecutorUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/ops")
@RequiredArgsConstructor
@Slf4j
public class OpsController {
    private final InstanceConfigUseCase instanceConfigUseCase;
    private final InstanceExecutorUseCase instanceExecutorUseCase;

    @PutMapping("/start")
    public BooleanWrapper start() {
        String instanceList = "admin-page admin-service api-platform-test api-webflux-gateway auth-api auth-face auth-face-test auth-gateway auth-gateway-test auth-manager auth-mq-job auth-security auth-service bee-release bull-assets-gateway bull-capital-gateway bull-prepose bull-prepose-baffle bull-prepose-debug bull-prepose-dispatch bull-prepose-file cash-a cash-b check-test customize-mock customize-test-2 dataanalysis eureka-dev eureka-standard eureka-test-one eureka-test-three eureka-test-two fincloud-loan-cif-admin fincloud-loan-contractseal-service fincloud-loan-credit-biz-service fincloud-loan-customer fincloud-loan-eureka-server fincloud-loan-insure-manage fincloud-loan-insure-service fincloud-loan-insure-task fincloud-loan-iou-repay-service fincloud-loan-iou-repay-task1 fincloud-loan-iou-report-service fincloud-loan-order-calculate-service fincloud-loan-order-contract-service fincloud-loan-order-contract-task fincloud-loan-order-loan-service fincloud-loan-order-loan-task fincloud-loan-order-manage-service fincloud-loan-order-manage-task fincloud-loan-pay-service fincloud-loan-pay-task fincloud-loan-productinfo-service fincloud-loan-quota-biz-service gateway-uat haidai-check hawk-ui iou-manage-new iou-manage-task jdy-api-test jdy-timing-test mall-sit mall-test manager market-api-uat market-service-uat master mkimbat-job mkmoln-api mkmtms-manager mkmtms-uat newmall onecard-auth-common onecard-customize-core onecard-deposit-devlop onecard-job-admin onecard-membership-uat onecard-message-api onecard-message-new onecard-order-codegen onecard-orderapi-test onecard-orderservice-dangban onecard-orderservice-test onecard-sms-manage-uat onecard-task onecard-yyapi-uat order-center-test order-task-test phoenix-api phoenix-test postloan-sit proxy pubapinew publicadmin quota-check quota-task scheduler-api te-telemarket-server te-tenant-user-service-uat te-timing-telemarketing-service-uat test-euraka unify-manager user-api-stable-yx user-api-stable2 user-kernel-test user-service-stable-yx user-service-test userinfo-new userinfo-test wk-check-test-01 wk-quota-product-new wk-quota-task-product wkadmin wkapinew wkapitask wkapp-dev wkapp-test wkconifg-test wkport-test-new wktrait-test xfk-rec-sys xiaofankasearch xxl-job-admin";
//
        final ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (String instance : instanceList.split(" ")) {
            try {
                final InstanceConfigDTO config = instanceConfigUseCase.getConfiguration("default", instance);
                if (config != null) {
                    executorService.execute(() -> {
                        instanceExecutorUseCase.startOrUpdate(new InstanceId(config.getId()));
                        log.info("{} started", instance);
                    });
                } else {
                    log.error("instance config not existed {}", instance);
                }
            } catch (Exception e) {
                log.error("query error {}", instance);
            }
        }


        return BooleanWrapper.TRUE;
    }
}
