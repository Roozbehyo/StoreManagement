package com.storemgmt.common.Repo;

import com.storemgmt.common.Db.ConnectionProvider;
import com.storemgmt.common.Entity.Seller;
import lombok.extern.log4j.Log4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Log4j
public class SellerRepository implements Repository<Seller, Integer> {
    private final Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public SellerRepository() throws Exception {
        connection = ConnectionProvider.getConnectionProvider().getConnection();
        log.info("Connected to database");
    }

    @Override
    public void save(Seller seller) throws Exception {
        seller.setId(ConnectionProvider.getConnectionProvider().nextId("seller_seq"));
        preparedStatement = connection.prepareStatement(
                "INSERT INTO SELLERS (ID, FIRSTNAME, LASTNAME, USERNAME, PASSWORD)" +
                        " VALUES (?,?,?,?,?)"
        );
        preparedStatement.setInt(1, seller.getId());
        preparedStatement.setString(2, seller.getFirstname());
        preparedStatement.setString(3, seller.getLastname());
        preparedStatement.setString(4, seller.getUsername());
        preparedStatement.setString(5, seller.getPassword());
        preparedStatement.execute();
    }

    @Override
    public void edit(Seller seller) throws Exception {
        preparedStatement = connection.prepareStatement(
                "UPDATE SELLERS " +
                        "SET FIRSTNAME=?, LASTNAME=?, PASSWORD=? " +
                        "WHERE ID=? AND USERNAME=?"
        );
        preparedStatement.setString(1, seller.getFirstname());
        preparedStatement.setString(2, seller.getLastname());
        preparedStatement.setString(3, seller.getPassword());
        preparedStatement.setInt(4, seller.getId());
        preparedStatement.setString(5, seller.getUsername());
        preparedStatement.execute();
    }

    @Override
    public void remove(Integer id) throws Exception {
    }

    @Override
    public List<Seller> findAll() throws Exception {
        preparedStatement = connection.prepareStatement(
                "SELECT * FROM SELLERS"
        );
        resultSet = preparedStatement.executeQuery();
        List<Seller> sellerList = new ArrayList<>();
        while (resultSet.next()) {
            Seller seller = Seller
                    .builder()
                    .id(resultSet.getInt("id"))
                    .firstname(resultSet.getString("firstname"))
                    .lastname(resultSet.getString("lastname"))
                    .username(resultSet.getString("username"))
                    .password(resultSet.getString("password"))
                    .build();
            sellerList.add(seller);
        }
        return sellerList;
    }

    @Override
    public Seller findById(Integer id) throws Exception {
        preparedStatement = connection.prepareStatement(
                "SELECT * FROM sellers WHERE id=?"
        );
        preparedStatement.setInt(1, id);
        resultSet = preparedStatement.executeQuery();

        Seller seller = null;
        if (resultSet.next()) {
            seller = Seller
                    .builder()
                    .id(resultSet.getInt("id"))
                    .firstname(resultSet.getString("firstname"))
                    .lastname(resultSet.getString("lastname"))
                    .username(resultSet.getString("username"))
                    .password(resultSet.getString("password"))
                    .build();
        }
        return seller;
    }

    public Integer findIdByUsername(String username) throws Exception {
        preparedStatement = connection.prepareStatement(
                "SELECT ID FROM SELLERS WHERE USERNAME=?"
        );
        preparedStatement.setString(1, username);
        resultSet = preparedStatement.executeQuery();
        return (resultSet.next()) ? resultSet.getInt("id") : null;
    }


    public List<Seller> findAllByFNameAndLName(String firstname, String lastname) throws Exception {
        preparedStatement = connection.prepareStatement(
                "SELECT * FROM SELLERS WHERE FIRSTNAME LIKE ? AND LASTNAME LIKE ? ORDER BY LASTNAME, FIRSTNAME"
        );
        preparedStatement.setString(1, firstname + "%");
        preparedStatement.setString(2, lastname + "%");
        ResultSet resultSet = preparedStatement.executeQuery();

        List<Seller> sellerList = new ArrayList<>();
        while (resultSet.next()) {
            Seller seller = Seller
                    .builder()
                    .id(resultSet.getInt("id"))
                    .firstname(resultSet.getString("firstname"))
                    .lastname(resultSet.getString("lastname"))
                    .username(resultSet.getString("username"))
                    .password(resultSet.getString("password"))
                    .build();
            sellerList.add(seller);
        }
        return sellerList;
    }

    public Seller findByUsernameAndPassword(String username, String password) throws Exception {
        preparedStatement = connection.prepareStatement(
                "SELECT * FROM SELLERS WHERE USERNAME=? AND PASSWORD=?"
        );
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        ResultSet resultSet = preparedStatement.executeQuery();

        Seller seller = null;
        if (resultSet.next()) {
            seller = Seller
                    .builder()
                    .id(resultSet.getInt("id"))
                    .firstname(resultSet.getString("firstname"))
                    .lastname(resultSet.getString("lastname"))
                    .username(resultSet.getString("username"))
                    .password(resultSet.getString("password"))
                    .build();
        }
        return seller;
    }

    @Override
    public void close() throws Exception {
        preparedStatement.close();
        connection.close();
        log.info("SellerRepo Connection closed");
    }
}
