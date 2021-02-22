package com.copernicus.opportunity.clients;

import com.copernicus.opportunity.dto.ContactDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("contact-service")
public interface ContactClient {
    @GetMapping("/contact/{id}")
    public ContactDTO getContact(@PathVariable Integer id);
}
