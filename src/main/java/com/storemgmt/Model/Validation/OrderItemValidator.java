package com.storemgmt.Model.Validation;

import com.storemgmt.Model.Entity.OrderItem;

import java.time.LocalDate;

public class OrderItemValidator {

    public boolean validateOrderItem(OrderItem orderItem) {
        boolean isValid = true;

        if (orderItem.getOrder().getId() == 0) {
            isValid = false;
        }
        if (orderItem.getProduct().getId() == 0) {
            isValid = false;
        }
        return isValid;
    }
}