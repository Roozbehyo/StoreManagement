package com.storemgmt.Model.Validation;

import com.storemgmt.Model.Entity.Product;

public class ProductValidator {

    public boolean validateProduct(Product product) {
        boolean isValid = true;

        if (!nameValidator(product.getName())) {
            isValid = false;
        }
        if (!priceValidator(String.valueOf(product.getPrice()))) {
            isValid = false;
        }
        return isValid;
    }

    public static boolean nameValidator(String name) {
        return name.matches("^[A-Za-zآ-ی]+( [A-Za-z0-9آ-ی]+)*$");
    }

    public static boolean priceValidator(String price) {
        return price.matches("^\\d+([,.]\\d{1,2})?$");
    }
}