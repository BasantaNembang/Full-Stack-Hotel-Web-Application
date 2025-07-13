package com.booking;

import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

public class TestServiceInstanceSupplier implements ServiceInstanceListSupplier {

    @Override
    public String getServiceId() {
        return "";
    }

    @Override
    public Flux<List<ServiceInstance>> get() {
        List<ServiceInstance> serviceInstances = new ArrayList<>();
        serviceInstances.add(new DefaultServiceInstance(
                "PAYMENT-SERVICE",
                "PAYMENT-SERVICE",
                "localhost",
                8080,
                false
        ));
       serviceInstances.add(new DefaultServiceInstance(
               "INVENTORY-SERVICE",
               "INVENTORY-SERVICE",
               "localhost",
               8080,
               false
       ));
        serviceInstances.add(new DefaultServiceInstance(
                "HOTEL-SERVICE",
                "HOTEL-SERVICE",
                "localhost",
                8080,
                false
        ));
        return Flux.just(serviceInstances);
    }

}
