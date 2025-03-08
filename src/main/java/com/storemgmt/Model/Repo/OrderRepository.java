package com.storemgmt.Model.Repo;

import com.storemgmt.Model.ConnectionProvider;
import com.storemgmt.Model.Entity.Customer;
import com.storemgmt.Model.Entity.Order;
import com.storemgmt.Model.Entity.Seller;
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
                "INSERT INTO ORDERS (ID, CUSTOMER_ID, SELLER_ID, ORDER_DATE)" +
                        " VALUES (?,?,?,?)"
        );
        preparedStatement.setInt(1, order.getId());
        preparedStatement.setInt(2, order.getCustomer().getId());
        preparedStatement.setInt(3, order.getSeller().getId());
        preparedStatement.setDate(4, Date.valueOf(order.getOrderDate()));
        preparedStatement.execute();
    }

    @Override
    public void edit(Order order) throws Exception {
        preparedStatement = connection.prepareStatement(
                "UPDATE ORDERS " +
                        "SET SELLER_ID=? " +
                        "WHERE ID=? "
        );
        preparedStatement.setInt(1, order.getSeller().getId());
        preparedStatement.setInt(2, order.getId());
        preparedStatement.execute();
    }

    @Override
    public void remove(Integer id) throws Exception {
    }

    @Override
    public List<Order> findAll() throws Exception {
        String sql = "SELECT ORDERS.ID AS ordersId, ORDER_DATE, " +
                "SELLERS.ID AS sellerId, SELLERS.FIRSTNAME AS sellerFName, SELLERS.LASTNAME AS sellerLName, " +
                "CUSTOMERS.ID AS customerId, CUSTOMERS.FIRSTNAME AS customerFName, CUSTOMERS.LASTNAME AS customerLName " +
                "FROM ORDERS " +
                "JOIN SELLERS ON ORDERS.SELLER_ID = SELLERS.ID " +
                "JOIN CUSTOMERS ON ORDERS.CUSTOMER_ID = CUSTOMERS.ID";
        preparedStatement = connection.prepareStatement(sql);
        resultSet = preparedStatement.executeQuery();
        List<Order> orderList = new ArrayList<>();
        while (resultSet.next()) {
            Seller seller = Seller
                    .builder()
                    .id(resultSet.getInt("sellerId"))
                    .firstname(resultSet.getString("sellerFName"))
                    .lastname(resultSet.getString("sellerLName"))
                    .build();
            Customer customer = Customer
                    .builder()
                    .id(resultSet.getInt("customerId"))
                    .firstname(resultSet.getString("customerFName"))
                    .lastname(resultSet.getString("customerLName"))
                    .build();
            Order order = Order
                    .builder()
                    .id(resultSet.getInt("ordersId"))
                    .orderDate(resultSet.getDate("order_date").toLocalDate())
                    .customer(customer)
                    .seller(seller)
                    .build();
            orderList.add(order);
        }
        return orderList;
    }

    @Override
    public Order findById(Integer id) throws Exception {
        String sql = "SELECT ORDERS.ID AS ordersId, ORDER_DATE, " +
                "SELLERS.ID AS sellerId, SELLERS.FIRSTNAME AS sellerFName, SELLERS.LASTNAME AS sellerLName, " +
                "CUSTOMERS.ID AS customerId, CUSTOMERS.FIRSTNAME AS customerFName, CUSTOMERS.LASTNAME AS customerLName " +
                "FROM ORDERS " +
                "JOIN SELLERS ON ORDERS.SELLER_ID = SELLERS.ID " +
                "JOIN CUSTOMERS ON ORDERS.CUSTOMER_ID = CUSTOMERS.ID WHERE ORDERS.ID = ?";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        resultSet = preparedStatement.executeQuery();

        Order order = null;
        if (resultSet.next()) {
            Seller seller = Seller
                    .builder()
                    .id(resultSet.getInt("sellerId"))
                    .firstname(resultSet.getString("sellerFName"))
                    .lastname(resultSet.getString("sellerLName"))
                    .build();
            Customer customer = Customer
                    .builder()
                    .id(resultSet.getInt("customerId"))
                    .firstname(resultSet.getString("customerFName"))
                    .lastname(resultSet.getString("customerLName"))
                    .build();
            order = Order
                    .builder()
                    .id(resultSet.getInt("ordersId"))
                    .orderDate(resultSet.getDate("order_date").toLocalDate())
                    .customer(customer)
                    .seller(seller)
                    .build();
        }
        return order;
    }

    @Override
    public void close() throws Exception {
        preparedStatement.close();
        connection.close();
        log.info("orderRepo Connection closed");
    }
}
