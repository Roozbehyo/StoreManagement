package com.storemgmt.Model.Validation;

import com.storemgmt.Model.Entity.BranchSeller;

public class BranchSellerValidator {

    public boolean validateBranchSeller(BranchSeller branchSeller) {
        boolean isValid = true;

        if (branchSeller.getStoreBranch().getId() == 0) {
            isValid = false;
        }
        if (branchSeller.getSeller().getId() == 0) {
            isValid = false;
        }
        return isValid;
    }
}