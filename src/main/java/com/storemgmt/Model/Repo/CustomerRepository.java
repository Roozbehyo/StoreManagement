package com.storemgmt.Model.Repo;

import com.storemgmt.Model.ConnectionProvider;
import com.storemgmt.Model.Entity.Customer;
import com.storemgmt.Model.Entity.Enum.Sex;
import lombok.extern.log4j.Log4j;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Log4j
public class CustomerRepository implements Repository<Customer, Integer> {
    private final Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public CustomerRepository() throws Exception {
        connection = ConnectionProvider.getConnectionProvider().getConnection();
        log.info("Connected to database");
    }

    @Override
    public void save(Customer customer) throws Exception {
        customer.setId(ConnectionProvider.getConnectionProvider().nextId("customer_seq"));
        preparedStatement = connection.prepareStatement(
                "INSERT INTO customers (id, firstname, lastname, birthdate, national_id, phone_num, sex)" +
                        " VALUES (?,?,?,?,?,?,?)"
        );
        preparedStatement.setInt(1, customer.getId());
        preparedStatement.setString(2, customer.getFirstname());
        preparedStatement.setString(3, customer.getLastname());
        preparedStatement.setDate(4, Date.valueOf((customer.getBirthdate()
        )));
        preparedStatement.setString(5, customer.getNationalId());
        preparedStatement.setString(6, customer.getPhoneNumber());
        preparedStatement.setString(7, String.valueOf(customer.getSex()));
        preparedStatement.execute();
    }

    @Override
    public void edit(Customer customer) throws Exception {
        preparedStatement = connection.prepareStatement(
                "UPDATE CUSTOMERS " +
                        "SET birthdate=?, phone_num=? " +
                        "WHERE id=?"
        );
        preparedStatement.setDate(1, Date.valueOf((customer.getBirthdate())));
        preparedStatement.setString(2, customer.getPhoneNumber());
        preparedStatement.setInt(3, customer.getId());
        preparedStatement.execute();
    }

    @Override
    public void remove(Integer id) throws Exception {
    }

    @Override
    public List<Customer> findAll() throws Exception {
        preparedStatement = connection.prepareStatement(
                "SELECT * FROM customers"
        );
        resultSet = preparedStatement.executeQuery();
        List<Customer> customerList = new ArrayList<>();
        while (resultSet.next()) {
            Customer customer = Customer
                    .builder()
                    .id(resultSet.getInt("id"))
                    .firstname(resultSet.getString("firstname"))
                    .lastname(resultSet.getString("lastname"))
                    .birthdate(resultSet.getDate("birthdate").toLocalDate())
                    .nationalId(resultSet.getString("national_id"))
                    .phoneNumber(resultSet.getString("phone_num"))
                    .sex(Sex.valueOf(resultSet.getString("sex")))
                    .build();
            customerList.add(customer);
        }
        return customerList;
    }

    @Override
    public Customer findById(Integer id) throws Exception {
        preparedStatement = connection.prepareStatement(
                "SELECT * FROM customers WHERE id=?"
        );
        preparedStatement.setInt(1, id);
        resultSet = preparedStatement.executeQuery();

        Customer customer = null;
        if(resultSet.next()) {
            customer = Customer
                    .builder()
                    .id(resultSet.getInt("id"))
                    .firstname(resultSet.getString("firstname"))
                    .lastname(resultSet.getString("lastname"))
                    .birthdate(resultSet.getDate("birthdate").toLocalDate())
                    .nationalId(resultSet.getString("national_id"))
                    .phoneNumber(resultSet.getString("phone_num"))
                    .sex(Sex.valueOf(resultSet.getString("sex")))
                    .build();
        }
        return customer;
    }

    @Override
    public void close() throws Exception {
        preparedStatement.close();
        connection.close();
        log.info("CustomerRepo Connection closed");
    }
}
