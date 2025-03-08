package com.storemgmt.Model.Repo;

import com.storemgmt.Model.ConnectionProvider;
import com.storemgmt.Model.Entity.Order;
import com.storemgmt.Model.Entity.OrderItem;
import com.storemgmt.Model.Entity.Product;
import lombok.extern.log4j.Log4j;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Log4j
public class OrderItemRepository implements Repository<OrderItem, Integer> {
    private final Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public OrderItemRepository() throws Exception {
        connection = ConnectionProvider.getConnectionProvider().getConnection();
        log.info("Connected to database");
    }

    @Override
    public void save(OrderItem orderItem) throws Exception {
        orderItem.setId(ConnectionProvider.getConnectionProvider().nextId("order_item_seq"));
        preparedStatement = connection.prepareStatement(
                "INSERT INTO ORDER_ITEMS(ID, ORDER_ID, PRODUCT_ID, QUANTITY)" +
                        " VALUES (?,?,?,?)"
        );
        preparedStatement.setInt(1, orderItem.getId());
        preparedStatement.setInt(2, orderItem.getOrder().getId());
        preparedStatement.setInt(3, orderItem.getProduct().getId());
        preparedStatement.setInt(4, orderItem.getQuantity());
        preparedStatement.execute();
    }

    @Override
    public void edit(OrderItem orderItem) throws Exception {
        preparedStatement = connection.prepareStatement(
                "UPDATE ORDER_ITEMS " +
                        "SET PRODUCT_ID=?, QUANTITY=? " +
                        "WHERE ID=? "
        );
        preparedStatement.setInt(1, orderItem.getProduct().getId());
        preparedStatement.setInt(2, orderItem.getQuantity());
        preparedStatement.setInt(3, orderItem.getId());
        preparedStatement.execute();
    }

    @Override
    public void remove(Integer id) throws Exception {
    }

    @Override
    public List<OrderItem> findAll() throws Exception {
        String sql = "SELECT ORDER_ITEMS.ID AS orderItemsId, QUANTITY, " +
                "ORDERS.ID AS ordersId, " +
                "PRODUCTS.ID AS productsId, PRODUCTS.NAME AS productName, PRODUCTS.PRICE " +
                "FROM ORDER_ITEMS " +
                "JOIN ORDERS ON ORDER_ITEMS.ORDER_ID = ORDERS.ID " +
                "JOIN PRODUCTS ON ORDER_ITEMS.PRODUCT_ID = PRODUCTS.ID";
        preparedStatement = connection.prepareStatement(sql);
        resultSet = preparedStatement.executeQuery();
        List<OrderItem> orderItemList = new ArrayList<>();
        while (resultSet.next()) {
            Order order = Order
                    .builder()
                    .id(resultSet.getInt("ordersId"))
                    .build();
            Product product = Product
                    .builder()
                    .id(resultSet.getInt("productsId"))
                    .name(resultSet.getString("productName"))
                    .price(resultSet.getFloat("price"))
                    .build();
            OrderItem orderItem = OrderItem
                    .builder()
                    .id(resultSet.getInt("orderItemsId"))
                    .quantity(resultSet.getInt("quantity"))
                    .order(order)
                    .product(product)
                    .build();
            orderItemList.add(orderItem);
        }
        return orderItemList;
    }

    @Override
    public OrderItem findById(Integer id) throws Exception {
        String sql = "SELECT ORDER_ITEMS.ID AS orderItemsId, QUANTITY, " +
                "ORDERS.ID AS ordersId, " +
                "PRODUCTS.ID AS productsId, PRODUCTS.NAME AS productName, PRODUCTS.PRICE " +
                "FROM ORDER_ITEMS " +
                "JOIN ORDERS ON ORDER_ITEMS.ORDER_ID = ORDERS.ID " +
                "JOIN PRODUCTS ON ORDER_ITEMS.PRODUCT_ID = PRODUCTS.ID WHERE ORDER_ITEMS.ID = ?";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        resultSet = preparedStatement.executeQuery();

        OrderItem orderItem = null;
        if (resultSet.next()) {
            Order order = Order
                    .builder()
                    .id(resultSet.getInt("ordersId"))
                    .build();
            Product product = Product
                    .builder()
                    .id(resultSet.getInt("productsId"))
                    .name(resultSet.getString("productName"))
                    .price(resultSet.getFloat("price"))
                    .build();
            orderItem = OrderItem
                    .builder()
                    .id(resultSet.getInt("orderItemsId"))
                    .quantity(resultSet.getInt("quantity"))
                    .order(order)
                    .product(product)
                    .build();
        }
        return orderItem;
    }

    @Override
    public void close() throws Exception {
        preparedStatement.close();
        connection.close();
        log.info("orderItemRepo Connection closed");
    }
}
