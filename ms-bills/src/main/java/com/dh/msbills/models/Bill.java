package com.dh.msbills.models;


import com.dh.msbills.dto.BillDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table
public class Bill {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String idBill;

    private String customerBill;

    private String productBill;

    private Double totalPrice;

    public static Bill fromDto(BillDto billDto)
    {
        Bill newBill = new Bill();
        newBill.setCustomerBill(billDto.getCustomerBill());
        newBill.setProductBill(billDto.getProductBill());
        newBill.setTotalPrice(billDto.getTotalPrice());
        return newBill;
    }
}
