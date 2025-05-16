package com.storemgmt.common.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@ToString
@SuperBuilder(toBuilder = true)
public class OrderItem {
    private int id;
    private Order order;
    private Product product;
    private int quantity;
}
