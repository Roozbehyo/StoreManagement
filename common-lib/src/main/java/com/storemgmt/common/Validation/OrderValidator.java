package com.storemgmt.common.Validation;

import com.storemgmt.common.Entity.Order;

import java.time.LocalDate;

public class OrderValidator {

    public void validateOrder(Order order) throws Exception {
        if (order.getCustomer().getId() == 0) {
            throw new Exception("Customer ID is required");
        }
        if (order.getSeller().getId() == 0) {
            throw new Exception("Seller ID is required");
        }
        if (order.getStoreBranch().getId() == 0) {
            throw new Exception("Store Branch ID is required");
        }
        if (order.getOrderDate() == null || order.getOrderDate().isAfter(LocalDate.now())) {
            throw new Exception("Order Date is required");
        }
    }
}