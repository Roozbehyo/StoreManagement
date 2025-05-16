package com.storemgmt.common.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@ToString
@SuperBuilder(toBuilder = true)
public class Inventory {
    private int id;
    private int quantity;
    private Product product;
    private StoreBranch storeBranch;
}
