package com.hodbenor.project.slot.machine.rest.client;

import com.hodbenor.project.slot.machine.data.beans.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class AccumulationClient {
    private final static String WIN_SLOT_MACHINE_URL = "/api/accumulation/win-slot-machine";
    private final DiscoveryClient discoveryClient;
    private final RestClient restClient;

    @Value("${accumulation.service.id}")
    private String accumulationServiceId;

    public AccumulationClient(DiscoveryClient discoveryClient, RestClient.Builder restClientBuilder) {
        this.discoveryClient = discoveryClient;
        this.restClient = restClientBuilder.build();
    }

    public WinSlotMachineResponse winSlotMachine(User user, List<Integer> digits) {
        ServiceInstance serviceInstance = discoveryClient.getInstances(accumulationServiceId).get(0);
        try {
            return restClient.post()
                    .uri(serviceInstance.getUri() + WIN_SLOT_MACHINE_URL)
                    .body(new WinSlotMachineRequest(user.id(), digits))
                    .retrieve()
                    .body(WinSlotMachineResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new WinSlotMachineResponse(Map.of());
    }
}