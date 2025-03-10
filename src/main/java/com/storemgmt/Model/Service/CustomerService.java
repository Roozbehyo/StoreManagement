package com.storemgmt.Model.Service;

import com.storemgmt.Model.Entity.Customer;
import com.storemgmt.Model.Repo.CustomerRepository;
import com.storemgmt.Model.Repo.ProductRepository;

import java.util.List;

public class CustomerService implements Service<Customer, Integer> {
    @Override
    public void save(Customer customer) throws Exception {
        if (!(customer.getBirthdate().getYear() >= 1980 && customer.getBirthdate().getYear() <= 2020)) {
            throw new Exception("Invalid Birth Year");
        }
        if (checkNationalIdExistence(customer.getNationalId())) {
            throw new Exception("The National Id is already registered");
        }
        if (checkPhoneNumberExistence(customer.getPhoneNumber())) {
            throw new Exception("The Phone Number is already registered");
        }
        try (CustomerRepository customerRepository = new CustomerRepository()) {
            customerRepository.save(customer);
        }
    }

    @Override
    public void edit(Customer customer) throws Exception {
        findById(customer.getId());
        if (!(customer.getBirthdate().getYear() >= 1980 && customer.getBirthdate().getYear() <= 2020)) {
            throw new Exception("Invalid Birth Year");
        }
        if (checkPhoneNumberExistence(customer.getPhoneNumber())) {
            throw new Exception("The Phone Number is already registered");
        }
        try (CustomerRepository customerRepository = new CustomerRepository()) {
            customerRepository.edit(customer);
        }
    }

    @Override
    public void remove(Integer id) throws Exception {
        findById(id);
        try (CustomerRepository customerRepository = new CustomerRepository()) {
            customerRepository.remove(id);
        }
    }

    @Override
    public List<Customer> findAll() throws Exception {
        try (CustomerRepository customerRepository = new CustomerRepository()) {
            List<Customer> customerList = customerRepository.findAll();
            if (customerList.isEmpty()) {
                throw new Exception("Customer not found");
            }
            return customerList;
        }
    }

    @Override
    public Customer findById(Integer id) throws Exception {
        try (CustomerRepository customerRepository = new CustomerRepository()) {
            Customer customer = customerRepository.findById(id);
            if (customer == null) {
                throw new Exception("Customer not found");
            }
            return customer;
        }
    }

    public List<Customer> findAllByFNameAndLName(String firstname, String lastname) throws Exception {
        try (CustomerRepository customerRepository = new CustomerRepository()) {
            List<Customer> customerList = customerRepository.findAllByFNameAndLName(firstname, lastname);
            if (customerList.isEmpty()) {
                throw new Exception("Customer not found");
            }
            return customerList;
        }
    }

    public boolean checkNationalIdExistence(String nationalId) throws Exception {
        try (CustomerRepository customerRepository = new CustomerRepository()) {
            String sql = " national_id=? ";
            Integer result = customerRepository.findIdByCondition(sql, nationalId);
            return result != null;
        }
    }

    public boolean checkPhoneNumberExistence(String phoneNumber) throws Exception {
        try (CustomerRepository customerRepository = new CustomerRepository()) {
            String sql = " phone_num=? ";
            Integer result = customerRepository.findIdByCondition(sql, phoneNumber);
            return result != null;
        }
    }
}
