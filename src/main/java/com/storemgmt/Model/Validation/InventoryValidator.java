package com.storemgmt.Model.Validation;

import com.storemgmt.Model.Entity.Inventory;

public class InventoryValidator {

    public boolean validateInventory(Inventory inventory) {
        boolean isValid = true;

        if (inventory.getStoreBranch().getId() == 0) {
            isValid = false;
        }
        if (inventory.getProduct().getId() == 0) {
            isValid = false;
        }
        return isValid;
    }
}