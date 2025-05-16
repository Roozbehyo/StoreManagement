package com.storemgmt.common.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@ToString
@SuperBuilder(toBuilder = true)
public class BranchSeller {
    private int id;
    private StoreBranch storeBranch;
    private Seller seller;
}
