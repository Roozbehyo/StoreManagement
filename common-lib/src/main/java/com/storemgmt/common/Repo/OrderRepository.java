package com.storemgmt.common.Repo;

import com.storemgmt.common.Db.ConnectionProvider;
import com.storemgmt.common.Entity.Customer;
import com.storemgmt.common.Entity.Order;
import com.storemgmt.common.Entity.Seller;
import com.storemgmt.common.Entity.StoreBranch;
import lombok.extern.log4j.Log4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Log4j
public class OrderRepository implements Repository<Order, Integer> {
    private final Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public OrderRepository() throws Exception {
        connection = ConnectionProvider.getConnectionProvider().getConnection();
        log.info("Connected to database");
    }

    @Override
    public void save(Order order) throws Exception {
        order.setId(ConnectionProvider.getConnectionProvider().nextId("order_seq"));
        preparedStatement = connection.prepareStatement(
                "INSERT INTO ORDERS (ID, CUSTOMER_ID, SELLER_ID, BRANCH_ID, ORDER_DATE)" +
                        " VALUES (?,?,?,?,?)"
        );
        preparedStatement.setInt(1, order.getId());
        preparedStatement.setInt(2, order.getCustomer().getId());
        preparedStatement.setInt(3, order.getSeller().getId());
        preparedStatement.setInt(4, order.getStoreBranch().getId());
        preparedStatement.setDate(5, Date.valueOf(order.getOrderDate()));
        preparedStatement.execute();
    }

    @Override
    public void edit(Order order) throws Exception {
        preparedStatement = connection.prepareStatement(
                "UPDATE ORDERS " +
                        "SET SELLER_ID=? " +
                        "WHERE ID=? AND BRANCH_ID=? AND ORDER_DATE=?"
        );
        preparedStatement.setInt(1, order.getSeller().getId());
        preparedStatement.setInt(2, order.getId());
        preparedStatement.setInt(3, order.getStoreBranch().getId());
        preparedStatement.setDate(4, Date.valueOf(order.getOrderDate()));
        preparedStatement.execute();
    }

    @Override
    public void remove(Integer id) throws Exception {
    }

    @Override
    public List<Order> findAll() throws Exception {
        String sql = "SELECT ORDERS.ID AS ordersId, ORDER_DATE, " +
                "SELLERS.FIRSTNAME AS sellerFName, SELLERS.LASTNAME AS sellerLName, " +
                "CUSTOMERS.FIRSTNAME AS customerFName, CUSTOMERS.LASTNAME AS customerLName, " +
                "STORE_BRANCH.BRANCH_NAME " +
                "FROM ORDERS " +
                "JOIN SELLERS ON ORDERS.SELLER_ID = SELLERS.ID " +
                "JOIN CUSTOMERS ON ORDERS.CUSTOMER_ID = CUSTOMERS.ID " +
                "JOIN STORE_BRANCH ON ORDERS.BRANCH_ID = STORE_BRANCH.ID";
        preparedStatement = connection.prepareStatement(sql);
        resultSet = preparedStatement.executeQuery();
        List<Order> orderList = new ArrayList<>();
        while (resultSet.next()) {
            Seller seller = Seller
                    .builder()
                    .firstname(resultSet.getString("sellerFName"))
                    .lastname(resultSet.getString("sellerLName"))
                    .build();
            Customer customer = Customer
                    .builder()
                    .firstname(resultSet.getString("customerFName"))
                    .lastname(resultSet.getString("customerLName"))
                    .build();
            StoreBranch storeBranch = StoreBranch
                    .builder()
                    .branchName(resultSet.getString("branch_name"))
                    .build();
            Order order = Order
                    .builder()
                    .id(resultSet.getInt("ordersId"))
                    .orderDate(resultSet.getDate("order_date").toLocalDate())
                    .customer(customer)
                    .seller(seller)
                    .storeBranch(storeBranch)
                    .build();
            orderList.add(order);
        }
        return orderList;
    }

    @Override
    public Order findById(Integer id) throws Exception {
        String sql = "SELECT ORDERS.ID AS ordersId, ORDER_DATE, " +
                "SELLERS.FIRSTNAME AS sellerFName, SELLERS.LASTNAME AS sellerLName, " +
                "CUSTOMERS.FIRSTNAME AS customerFName, CUSTOMERS.LASTNAME AS customerLName, " +
                "STORE_BRANCH.ID AS storeBranchId, STORE_BRANCH.BRANCH_NAME " +
                "FROM ORDERS " +
                "JOIN SELLERS ON ORDERS.SELLER_ID = SELLERS.ID " +
                "JOIN CUSTOMERS ON ORDERS.CUSTOMER_ID = CUSTOMERS.ID " +
                "JOIN STORE_BRANCH ON ORDERS.BRANCH_ID = STORE_BRANCH.ID " +
                "WHERE ORDERS.ID = ?";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        resultSet = preparedStatement.executeQuery();

        Order order = null;
        if (resultSet.next()) {
            Seller seller = Seller
                    .builder()
                    .firstname(resultSet.getString("sellerFName"))
                    .lastname(resultSet.getString("sellerLName"))
                    .build();
            Customer customer = Customer
                    .builder()
                    .firstname(resultSet.getString("customerFName"))
                    .lastname(resultSet.getString("customerLName"))
                    .build();
            StoreBranch storeBranch = StoreBranch
                    .builder()
                    .id(resultSet.getInt("storeBranchId"))
                    .branchName(resultSet.getString("branch_name"))
                    .build();
            order = Order
                    .builder()
                    .id(resultSet.getInt("ordersId"))
                    .orderDate(resultSet.getDate("order_date").toLocalDate())
                    .customer(customer)
                    .seller(seller)
                    .storeBranch(storeBranch)
                    .build();
        }
        return order;
    }

    public List<Order> findAllByCustomerFNameAndLName(String firstname, String lastname) throws Exception {
        String sql = "SELECT ORDERS.ID AS ordersId, ORDER_DATE, " +
                "SELLERS.FIRSTNAME AS sellerFName, SELLERS.LASTNAME AS sellerLName, " +
                "CUSTOMERS.FIRSTNAME AS customerFName, CUSTOMERS.LASTNAME AS customerLName, " +
                "STORE_BRANCH.BRANCH_NAME " +
                "FROM ORDERS " +
                "JOIN SELLERS ON ORDERS.SELLER_ID = SELLERS.ID " +
                "JOIN CUSTOMERS ON ORDERS.CUSTOMER_ID = CUSTOMERS.ID " +
                "JOIN STORE_BRANCH ON ORDERS.BRANCH_ID = STORE_BRANCH.ID " +
                "WHERE CUSTOMERS.FIRSTNAME LIKE ? AND CUSTOMERS.LASTNAME LIKE ?";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, firstname + "%");
        preparedStatement.setString(2, lastname + "%");
        List<Order> orderList = new ArrayList<>();
        while (resultSet.next()) {
            Seller seller = Seller
                    .builder()
                    .firstname(resultSet.getString("sellerFName"))
                    .lastname(resultSet.getString("sellerLName"))
                    .build();
            Customer customer = Customer
                    .builder()
                    .firstname(resultSet.getString("customerFName"))
                    .lastname(resultSet.getString("customerLName"))
                    .build();
            StoreBranch storeBranch = StoreBranch
                    .builder()
                    .branchName(resultSet.getString("branch_name"))
                    .build();
            Order order = Order
                    .builder()
                    .id(resultSet.getInt("ordersId"))
                    .orderDate(resultSet.getDate("order_date").toLocalDate())
                    .customer(customer)
                    .seller(seller)
                    .storeBranch(storeBranch)
                    .build();
            orderList.add(order);
        }
        return orderList;
    }

    public List<Order> findAllBySellerFNameAndLName(String firstname, String lastname) throws Exception {
        String sql = "SELECT ORDERS.ID AS ordersId, ORDER_DATE, " +
                "SELLERS.FIRSTNAME AS sellerFName, SELLERS.LASTNAME AS sellerLName, " +
                "CUSTOMERS.FIRSTNAME AS customerFName, CUSTOMERS.LASTNAME AS customerLName, " +
                "STORE_BRANCH.BRANCH_NAME " +
                "FROM ORDERS " +
                "JOIN SELLERS ON ORDERS.SELLER_ID = SELLERS.ID " +
                "JOIN CUSTOMERS ON ORDERS.CUSTOMER_ID = CUSTOMERS.ID " +
                "JOIN STORE_BRANCH ON ORDERS.BRANCH_ID = STORE_BRANCH.ID " +
                "WHERE SELLERS.FIRSTNAME LIKE ? AND SELLERS.LASTNAME LIKE ?";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, firstname + "%");
        preparedStatement.setString(2, lastname + "%");
        resultSet = preparedStatement.executeQuery();
        List<Order> orderList = new ArrayList<>();
        while (resultSet.next()) {
            Seller seller = Seller
                    .builder()
                    .firstname(resultSet.getString("sellerFName"))
                    .lastname(resultSet.getString("sellerLName"))
                    .build();
            Customer customer = Customer
                    .builder()
                    .firstname(resultSet.getString("customerFName"))
                    .lastname(resultSet.getString("customerLName"))
                    .build();
            StoreBranch storeBranch = StoreBranch
                    .builder()
                    .branchName(resultSet.getString("branch_name"))
                    .build();
            Order order = Order
                    .builder()
                    .id(resultSet.getInt("ordersId"))
                    .orderDate(resultSet.getDate("order_date").toLocalDate())
                    .customer(customer)
                    .seller(seller)
                    .storeBranch(storeBranch)
                    .build();
            orderList.add(order);
        }
        return orderList;
    }

    @Override
    public void close() throws Exception {
        preparedStatement.close();
        connection.close();
        log.info("orderRepo Connection closed");
    }
}
