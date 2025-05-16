package com.storemgmt.common.Repo;

import com.storemgmt.common.Db.ConnectionProvider;
import com.storemgmt.common.Entity.Product;
import lombok.extern.log4j.Log4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Log4j
public class ProductRepository implements Repository<Product, Integer> {
    private final Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public ProductRepository() throws Exception {
        connection = ConnectionProvider.getConnectionProvider().getConnection();
        log.info("Connected to database");
    }

    @Override
    public void save(Product product) throws Exception {
        product.setId(ConnectionProvider.getConnectionProvider().nextId("product_seq"));
        preparedStatement = connection.prepareStatement(
                "INSERT INTO PRODUCTS (ID, NAME, PRICE)" +
                        " VALUES (?,?,?)"
        );
        preparedStatement.setInt(1, product.getId());
        preparedStatement.setString(2, product.getName());
        preparedStatement.setFloat(3, product.getPrice());
        preparedStatement.execute();
    }

    @Override
    public void edit(Product product) throws Exception {
        preparedStatement = connection.prepareStatement(
                "UPDATE PRODUCTS " +
                        "SET NAME=?, PRICE=? " +
                        "WHERE ID=? AND IS_DELETED =0"
        );
        preparedStatement.setString(1, product.getName());
        preparedStatement.setFloat(2, product.getPrice());
        preparedStatement.setInt(3, product.getId());
        preparedStatement.execute();
    }

    @Override
    public void remove(Integer id) throws Exception {
        preparedStatement = connection.prepareStatement(
                "UPDATE PRODUCTS " +
                        "SET IS_DELETED=1 " +
                        "WHERE ID=? "
        );
        preparedStatement.setInt(1, id);
        preparedStatement.execute();
    }

    @Override
    public List<Product> findAll() throws Exception {
        preparedStatement = connection.prepareStatement(
                "SELECT * FROM PRODUCTS WHERE IS_DELETED=0"
        );
        resultSet = preparedStatement.executeQuery();
        List<Product> productList = new ArrayList<>();
        while (resultSet.next()) {
            Product product = Product
                    .builder()
                    .id(resultSet.getInt("id"))
                    .name(resultSet.getString("name"))
                    .price(resultSet.getFloat("price"))
                    .build();
            productList.add(product);
        }
        return productList;
    }

    @Override
    public Product findById(Integer id) throws Exception {
        preparedStatement = connection.prepareStatement(
                "SELECT * FROM PRODUCTS WHERE IS_DELETED=0 AND ID=?"
        );
        preparedStatement.setInt(1, id);
        resultSet = preparedStatement.executeQuery();

        Product product = null;
        if (resultSet.next()) {
            product = Product
                    .builder()
                    .id(resultSet.getInt("id"))
                    .name(resultSet.getString("name"))
                    .price(resultSet.getFloat("price"))
                    .build();
        }
        return product;
    }

    public List<Product> findAllByName(String name) throws Exception {
        preparedStatement = connection.prepareStatement(
                "SELECT * FROM PRODUCTS WHERE NAME LIKE ? AND IS_DELETED =0 ORDER BY NAME"
        );
        preparedStatement.setString(1, name + "%");
        ResultSet resultSet = preparedStatement.executeQuery();

        List<Product> productList = new ArrayList<>();
        while (resultSet.next()) {
            Product product = Product
                    .builder()
                    .id(resultSet.getInt("id"))
                    .name(resultSet.getString("name"))
                    .price(resultSet.getFloat("price"))
                    .build();
            productList.add(product);
        }
        return productList;
    }

    @Override
    public void close() throws Exception {
        preparedStatement.close();
        connection.close();
        log.info("productRepo Connection closed");
    }
}
