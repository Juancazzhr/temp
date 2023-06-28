package com.dh.msbills.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BillDto {
    String customerBill;
    String productBill;
    Double totalPrice;
}
