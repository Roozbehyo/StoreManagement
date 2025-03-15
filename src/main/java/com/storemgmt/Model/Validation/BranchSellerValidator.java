package com.storemgmt.Model.Validation;

import com.storemgmt.Model.Entity.BranchSeller;

public class BranchSellerValidator {

    public void validateBranchSeller(BranchSeller branchSeller) throws Exception {
        if (branchSeller.getStoreBranch().getId() == 0) {
            throw new Exception("Store branch Id is required");
        }
        if (branchSeller.getSeller().getId() == 0) {
            throw new Exception("Seller Id is required");
        }
    }
}