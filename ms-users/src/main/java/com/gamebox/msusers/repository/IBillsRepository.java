package com.gamebox.msusers.repository;

import com.gamebox.msusers.configuration.OpenFeignConfig;
import com.gamebox.msusers.dto.BillDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "ms-bills",
        configuration = OpenFeignConfig.class)
public interface IBillsRepository {

    @GetMapping(path = "/api/v1/bills")
    List<BillDto> getAllByCustomerId(@RequestParam(name = "customer_id")
                                     String customerId);
}
