package com.sheng.springcloud.controller;

import com.sheng.springcloud.entities.CommonResult;
import com.sheng.springcloud.entities.Payment;
import com.sheng.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Slf4j
public class PaymentController {
    @Resource
    PaymentService paymentService;

    @Value("${server.port}")
    private String serverId;

    @Resource
    private DiscoveryClient discoveryClient;

    /**
     * 插入
     * @param payment
     * @return
     */
    @PostMapping("/payment/create")
    public CommonResult create(@RequestBody Payment payment){
        int result = paymentService.create(payment);
        log.info("***********插入成功" + result);

        if (result > 0){
            return new CommonResult(200,"success   服务的是：" + serverId, result);
        }else {
            return new CommonResult(444,"fail",null);
        }
    }

    @GetMapping("/payment/get/{id}")
    public CommonResult getPaymentById(@PathVariable("id") Long id){
        Payment paymentById = paymentService.getPaymentById(id);
        log.info("**查询结果" + paymentById);

        if (paymentById != null){
            return new CommonResult(200,"select success  服务的是：" + serverId, paymentById);
        }else {
            return new CommonResult(444,"对于"+id+"没有查询记录",null);
        }
    }

    @GetMapping("/payment/discovery")
    public Object discovery(){
        List<String> services = discoveryClient.getServices();
        for (String service : services) {
            log.info("*********元素："+service);
        }

        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        for (ServiceInstance instance : instances) {
            log.info(instance.getServiceId() + "\t" + instance.getUri() + "\t" + instance.getPort());
        }
        System.out.println(discoveryClient.toString());
        return discoveryClient;
    }


}
