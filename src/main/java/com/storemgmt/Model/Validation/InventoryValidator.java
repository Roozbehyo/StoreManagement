package com.storemgmt.Model.Validation;

import com.storemgmt.Model.Entity.Inventory;

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