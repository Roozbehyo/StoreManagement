package com.storemgmt.Model.Validation;

import com.storemgmt.Model.Entity.Seller;

public class SellerValidator {

    public boolean validateSeller(Seller seller) {
        boolean isValid = true;

        if (!nameValidator(seller.getFirstname())) {
            isValid = false;
        }
        if (!nameValidator(seller.getLastname())) {
            isValid = false;
        }
        if (!usernameValidator(seller.getUsername())) {
            isValid = false;
        }
        if (!passwordValidator(seller.getPassword())) {
            isValid = false;
        }
        return isValid;
    }

    public static boolean nameValidator(String name) {
        return name.matches("^[A-Za-zآ-ی]+( [A-Za-zآ-ی]+)?$");
    }

    public static boolean usernameValidator(String username) {
        return username.matches("^[A-Za-z0-9_]{1,15}$");
    }

    public static boolean passwordValidator(String password) {
        return password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,15}$");
    }
}