package com.storemgmt.Model.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@ToString
@SuperBuilder(toBuilder = true)
public class StoreBranch {
    private int id;
    private String branchName;
    private Seller seller;
}
