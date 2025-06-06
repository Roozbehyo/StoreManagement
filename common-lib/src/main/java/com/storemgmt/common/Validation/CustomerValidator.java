package com.storemgmt.common.Validation;

import com.storemgmt.common.Entity.Customer;

public class CustomerValidator {

    public void validateCustomer(Customer customer) throws Exception {
        if (!nameValidator(customer.getFirstname())) {
            throw new Exception("Invalid First Name ");
        }
        if (!nameValidator(customer.getLastname())) {
            throw new Exception("Invalid Last Name ");
        }
        if (!birthdateValidator(String.valueOf(customer.getBirthdate()))) {
            throw new Exception("Invalid Birth Date ");
        }
        if (!sexValidator(String.valueOf(customer.getSex()))) {
            throw new Exception("Invalid Sex Option ");
        }
        if (!nationalIdValidator(customer.getNationalId())) {
            throw new Exception("Invalid National Id ");
        }
        if (!phoneValidator(customer.getPhoneNumber())) {
            throw new Exception("Invalid Phone Number ");
        }
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
        if (nationalId == null) {
            return false;
        }

        return nationalId.matches("\\d{10}$");
    }

    private boolean phoneValidator(String phone) {
        if (phone == null) {
            return false;
        }

        return phone.matches("09\\d{9}");
    }
}