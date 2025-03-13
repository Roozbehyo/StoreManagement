package com.storemgmt.Model.Validation;

import com.storemgmt.Model.Entity.OrderItem;

public class OrderItemValidator {

    public void validateOrderItem(OrderItem orderItem) throws Exception {
        if (orderItem.getOrder().getId() == 0) {
            throw new Exception("Order ID is required");
        }
        if (orderItem.getProduct().getId() == 0) {
            throw new Exception("Product ID is required");
        }
    }
}