package com.storemgmt.common.Repo;

import com.storemgmt.common.Db.ConnectionProvider;
import com.storemgmt.common.Entity.Customer;
import com.storemgmt.common.Entity.Enum.Sex;
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
                "INSERT INTO CUSTOMERS (ID, FIRSTNAME, LASTNAME, BIRTHDATE, NATIONAL_ID, PHONE_NUM, SEX)" +
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
                        "SET BIRTHDATE=?, PHONE_NUM=? " +
                        "WHERE ID=? AND IS_DELETED=0"
        );
        preparedStatement.setDate(1, Date.valueOf((customer.getBirthdate())));
        preparedStatement.setString(2, customer.getPhoneNumber());
        preparedStatement.setInt(3, customer.getId());
        preparedStatement.execute();
    }

    @Override
    public void remove(Integer id) throws Exception {
        preparedStatement = connection.prepareStatement(
                "UPDATE CUSTOMERS " +
                        "SET IS_DELETED=1 " +
                        "WHERE ID=? "
        );
        preparedStatement.setInt(1, id);
        preparedStatement.execute();
    }

    @Override
    public List<Customer> findAll() throws Exception {
        preparedStatement = connection.prepareStatement(
                "SELECT * FROM CUSTOMERS WHERE IS_DELETED=0 ORDER BY ID"
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
                "SELECT * FROM CUSTOMERS WHERE ID=? AND IS_DELETED=0"
        );
        preparedStatement.setInt(1, id);
        resultSet = preparedStatement.executeQuery();

        Customer customer = null;
        if (resultSet.next()) {
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

    public List<Customer> findAllByFNameAndLName(String firstname, String lastname) throws Exception {
        preparedStatement = connection.prepareStatement(
                "SELECT * FROM CUSTOMERS WHERE IS_DELETED=0 AND " +
                        "FIRSTNAME LIKE ? AND LASTNAME LIKE ? ORDER BY LASTNAME, FIRSTNAME"
        );
        preparedStatement.setString(1,  firstname + "%");
        preparedStatement.setString(2,  lastname + "%");
        ResultSet resultSet = preparedStatement.executeQuery();

        List<Customer> customerList = new ArrayList<>();
        while(resultSet.next()) {
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

    public Integer findIdByCondition(String sqlWhereClause, String value) throws Exception {
        preparedStatement = connection.prepareStatement(
                "SELECT * FROM customers WHERE IS_DELETED =0 AND " + sqlWhereClause
        );
        preparedStatement.setString(1, value);
        resultSet = preparedStatement.executeQuery();
        return (resultSet.next()) ? resultSet.getInt("id") : null;
    }

    @Override
    public void close() throws Exception {
        preparedStatement.close();
        connection.close();
        log.info("CustomerRepo Connection closed");
    }
}
