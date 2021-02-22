package com.copernicus.opportunity.clients;

import com.copernicus.opportunity.dto.AccountDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("account-service")
public interface AccountClient {
    @GetMapping("/account/{id}")
    AccountDTO getAccount(@PathVariable(name = "id") Integer id);
}
