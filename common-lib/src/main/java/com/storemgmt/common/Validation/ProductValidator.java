package com.storemgmt.common.Validation;

import com.storemgmt.common.Entity.Product;

public class ProductValidator {

    public void validateProduct(Product product) throws Exception {
        if (!nameValidator(product.getName())) {
            throw new Exception("Invalid product name");
        }
        if (!priceValidator(String.valueOf(product.getPrice()))) {
            throw new Exception("Invalid product price");
        }
    }

    public static boolean nameValidator(String name) {
        return name.matches("^[A-Za-zآ-ی]+( [A-Za-z0-9آ-ی]+)*$");
    }

    public static boolean priceValidator(String price) {
        return price.matches("^\\d+([,.]\\d{1,2})?$");
    }
}