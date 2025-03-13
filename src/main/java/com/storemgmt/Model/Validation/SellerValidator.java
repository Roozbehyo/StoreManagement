package com.storemgmt.Model.Validation;

import com.storemgmt.Model.Entity.Seller;

public class SellerValidator {

    public void validateSeller(Seller seller) throws Exception {
        if (!nameValidator(seller.getFirstname())) {
            throw new Exception("Invalid First Name ");
        }
        if (!nameValidator(seller.getLastname())) {
            throw new Exception("Invalid Last Name ");
        }
        if (!usernameValidator(seller.getUsername())) {
            throw new Exception("Invalid Username ");
        }
        if (!passwordValidator(seller.getPassword())) {
            throw new Exception("Invalid Password ");
        }
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