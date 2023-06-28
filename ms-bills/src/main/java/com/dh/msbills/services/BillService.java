package com.dh.msbills.services;

import com.dh.msbills.dto.BillDto;
import com.dh.msbills.models.Bill;
import com.dh.msbills.repositories.BillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BillService {

    private final BillRepository repository;

    public List<Bill> getAllBill() {
        return repository.findAll();
    }
    public List<Bill> getAllByCustomerId(String customerId) {
        return repository.findAllByCustomerBill(customerId);
    }

    public Bill save(BillDto billDto) {
        Bill billToSave = Bill.fromDto(billDto);
        return repository.save(billToSave);
    }
}
