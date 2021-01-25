package com.oc.hawk.container.runtime.port.driving.rest;

import com.oc.hawk.common.spring.mvc.BooleanWrapper;
import com.oc.hawk.container.api.dto.InstanceConfigDTO;
import com.oc.hawk.container.domain.model.runtime.config.InstanceId;
import com.oc.hawk.container.runtime.application.instance.InstanceConfigUseCase;
import com.oc.hawk.container.runtime.application.instance.InstanceExecutorUseCase;
import com.oc.hawk.container.runtime.port.driven.persistence.JpaInstanceConfigurationRepository;
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
    private final JpaInstanceConfigurationRepository jpaInstanceConfigurationRepository;
    private final InstanceConfigUseCase instanceConfigUseCase;
    private final InstanceExecutorUseCase instanceExecutorUseCase;

    @PutMapping("/start")
    public BooleanWrapper start() {
        String instanceList = "admin-page,admin-service, annual-goods-festival,api-platform-test,api-webflux-gateway,auth-api,auth-api-test,auth-face,auth-face-test,auth-gateway,auth-gateway-test,auth-manager,auth-mq-job,auth-others,auth-security,auth-service,bee-debug,bee-release,bi,bull-assets-gateway,bull-capital-gateway,bull-prepose,bull-prepose-baffle,bull-prepose-debug,bull-prepose-dispatch,bull-prepose-file,cash-a,cash-b,check-b,check-task-api,customize-kafka-test,customize-mock,customize-test-2,dataanalysis,demo30,dev,dev2,eureka-dev,eureka-standard,eureka-test-one,eureka-test-three,eureka-test-two,fincloud-loan-cif-admin,fincloud-loan-contractseal-service,fincloud-loan-credit-biz-service,fincloud-loan-customer,fincloud-loan-eureka-server,fincloud-loan-insure-service,fincloud-loan-insure-task,fincloud-loan-iou-cancel-service,fincloud-loan-iou-manage-service,fincloud-loan-iou-manage-task,fincloud-loan-iou-repay-service,fincloud-loan-iou-repay-task,fincloud-loan-iou-report-service,fincloud-loan-order-calculate-service,fincloud-loan-order-contract-service,fincloud-loan-order-contract-task,fincloud-loan-order-loan-service,fincloud-loan-order-loan-task,fincloud-loan-order-manage-service,fincloud-loan-order-manage-task,fincloud-loan-pay-service,fincloud-loan-productinfo-service,fincloud-loan-quota-biz-service,fslopen,gateway-test,gateway-uat,haidai-check,haidai-check-branch,honeycomb-master,initdata-test,jdy-api-test,jdy-timing-test,jf-portal-admin,jf-portal-provider,kafka,kepler-ph-bi,kunlin-drl,mall-gateway,mall-test,manager,manager-b,market-api-uat,market-service-uat,master,mkmbat-uat,mkmoln-uat,mkmtms-uat,newmall,onecard-auth-common,onecard-auth-common-test,onecard-canary-service-test,onecard-customize-core,onecard-deposit-devlop,onecard-job-admin,onecard-membership-uat,onecard-message-new,onecard-order-codegen,onecard-order-datacenter,onecard-orderapi-overduemsg,onecard-orderapi-test,onecard-orderservice-dangban,onecard-orderservice-test,onecard-report-master,onecard-sms-manage-uat,onecard-task,onecard-yyapi-uat,order-center-test,order-status,order-task-test,phoenix-test,postloan-sit,proxy,pubapi-sit,pubapinew,publicadmin,quota-check,quota-check-task,quota-task,release1,risk-interface-remove,rule-engine,rule-engine-old,scheduler-api,springboot-adminservice,te-backend-manage-uat,te-site-separate-uat,te-telemarket-server,te-tenant-user-service-uat,unify-manager,user-api-stable-yx,user-api-stable2,user-kernel-test,user-service-stable-yx,user-service-test,userinfo-test,wk-check-jgzj,wk-check-test-01,wk-quota-product-new,wk-quota-task-product,wkadmin,wkadminbasesql,wkadminstable,wkapi-dev,wkapinew,wkapitask,wkapp-test,wkconifg-test,wkport-test-new,wktrait-dev,wktrait-test,wsk-kafka,wsk-test,xfk-rec-sys,xiaofankasearch,xxl-job-admin";
//
        final ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (String instance : instanceList.split(",")) {
            try {
                final InstanceConfigDTO config = instanceConfigUseCase.getConfiguration("default", instance);
                if (config != null) {
//                    instanceExecutorUseCase.startOrUpdate(new InstanceId(config.getId()));
//                    log.info("{} started", instance);
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
//            instanceExecutorUseCase.startOrUpdate(config.getId());
        }


        return BooleanWrapper.TRUE;
    }
}
