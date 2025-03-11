package com.storemgmt.Model.Validation;

import com.storemgmt.Model.Entity.Order;

import java.time.LocalDate;

public class OrderValidator {

    public boolean validateOrder(Order order) {
        boolean isValid = true;

        if (order.getCustomer().getId() == 0) {
            isValid = false;
        }
        if (order.getSeller().getId() == 0) {
            isValid = false;
        }
        if (order.getStoreBranch().getId() == 0) {
            isValid = false;
        }
        if (order.getOrderDate() == null || order.getOrderDate().isAfter(LocalDate.now())) {
            isValid = false;
        }
        return isValid;
    }
}