package com.storemgmt.common.Validation;

import com.storemgmt.common.Entity.Inventory;

public class InventoryValidator {

    public void validateInventory(Inventory inventory) throws Exception{
        if (inventory.getStoreBranch().getId() == 0) {
            throw new Exception("Store Branch Id is required");
        }
        if (inventory.getProduct().getId() == 0) {
            throw new Exception("Product Id is required");
        }
    }
}