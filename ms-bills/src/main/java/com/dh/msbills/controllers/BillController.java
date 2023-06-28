package com.dh.msbills.controllers;

import com.dh.msbills.dto.BillDto;
import com.dh.msbills.models.Bill;
import com.dh.msbills.services.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bills")
@RequiredArgsConstructor
public class BillController {

    private final BillService service;

    @GetMapping("/all")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Bill>> getAll() {
        return ResponseEntity.ok().body(service.getAllBill());
    }

    // TODO: Implement logic
    @PostMapping
    @PreAuthorize("hasAuthority('GROUP_PROVIDERS')")
    public ResponseEntity<Bill> create(@RequestBody BillDto billDto) {
        return ResponseEntity.ok(service.save(billDto));
    }

    @GetMapping
    public ResponseEntity<List<Bill>> getAllByCustomerId(@RequestParam(name = "customer_id")
                                                         String customerId) {
        return ResponseEntity.ok(service.getAllByCustomerId(customerId));
    }
}
