package com.storemgmt.Model.Validation;

import com.storemgmt.Model.Entity.Customer;

public class CustomerValidator {

    public boolean validateCustomer(Customer customer) {
        boolean isValid = true;

        if (!nameValidator(customer.getFirstname())) {
            isValid = false;
        }
        if (!nameValidator(customer.getLastname())) {
            isValid = false;
        }
        if (!birthdateValidator(String.valueOf(customer.getBirthdate()))) {
            isValid = false;
        }
        if (!sexValidator(String.valueOf(customer.getSex()))) {
            isValid = false;
        }
        if (!nationalIdValidator(customer.getNationalId())) {
            isValid = false;
        }
        if (!phoneValidator(customer.getPhoneNumber())) {
            isValid = false;
        }

        return isValid;
    }

    public static boolean nameValidator(String name) {
        return name.matches("^[A-Za-zآ-ی]+( [A-Za-zآ-ی]+)?$");
    }

    public static boolean birthdateValidator(String birthdate) {
        return birthdate.matches("^\\d{4}[-/](0[1-9]|1[0-2])[-/](0[1-9]|[12][0-9]|3[01])$");
    }

    public static boolean sexValidator(String sex) {
        return sex.matches("^(MALE|FEMALE)$");
    }

    private boolean nationalIdValidator(String nationalId) {
        if (nationalId == null || nationalId.isBlank()) {
            return false;
        }

        return nationalId.matches("\\d{10}$");
    }

    private boolean phoneValidator(String phone) {
        if (phone == null || phone.isBlank()) {
            return false;
        }

        return phone.matches("09\\d{9}");
    }
}