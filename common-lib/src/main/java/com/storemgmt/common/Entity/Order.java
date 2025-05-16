package com.storemgmt.common.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Setter
@Getter
@ToString
@SuperBuilder(toBuilder = true)
public class Order {
    private int id;
    private LocalDate orderDate;
    private Customer customer;
    private Seller seller;
    private StoreBranch storeBranch;
}
