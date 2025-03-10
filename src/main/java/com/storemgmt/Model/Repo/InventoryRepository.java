package com.storemgmt.Model.Repo;

import com.storemgmt.Model.ConnectionProvider;
import com.storemgmt.Model.Entity.Inventory;
import com.storemgmt.Model.Entity.Product;
import com.storemgmt.Model.Entity.StoreBranch;
import lombok.extern.log4j.Log4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Log4j
public class InventoryRepository implements Repository<Inventory, Integer> {
    private final Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public InventoryRepository() throws Exception {
        connection = ConnectionProvider.getConnectionProvider().getConnection();
        log.info("Connected to database");
    }

    @Override
    public void save(Inventory inventory) throws Exception {
        inventory.setId(ConnectionProvider.getConnectionProvider().nextId("inventory_seq"));
        preparedStatement = connection.prepareStatement(
                "INSERT INTO INVENTORY(ID, PRODUCT_ID, BRANCH_ID, QUANTITY)" +
                        " VALUES (?,?,?,?)"
        );
        preparedStatement.setInt(1, inventory.getId());
        preparedStatement.setInt(2, inventory.getProduct().getId());
        preparedStatement.setInt(3, inventory.getStoreBranch().getId());
        preparedStatement.setInt(4, inventory.getQuantity());
        preparedStatement.execute();
    }

    @Override
    public void edit(Inventory inventory) throws Exception {
        preparedStatement = connection.prepareStatement(
                "UPDATE INVENTORY " +
                        "SET BRANCH_ID=?, QUANTITY=? " +
                        "WHERE ID=? AND PRODUCT_ID=?"
        );
        preparedStatement.setInt(1, inventory.getStoreBranch().getId());
        preparedStatement.setInt(2, inventory.getQuantity());
        preparedStatement.setInt(3, inventory.getId());
        preparedStatement.setInt(4, inventory.getProduct().getId());
        preparedStatement.execute();
    }

    @Override
    public void remove(Integer id) throws Exception {
    }

    @Override
    public List<Inventory> findAll() throws Exception {
        String sql = "SELECT INVENTORY.ID AS inventoryId, QUANTITY, " +
                "PRODUCTS.ID AS productsId, PRODUCTS.NAME AS productName, " +
                "STORE_BRANCH.ID AS storeBranchId, STORE_BRANCH.BRANCH_NAME " +
                "FROM INVENTORY " +
                "JOIN PRODUCTS ON INVENTORY.PRODUCT_ID = PRODUCTS.ID " +
                "JOIN STORE_BRANCH ON INVENTORY.BRANCH_ID = STORE_BRANCH.ID " +
                "WHERE PRODUCTS.IS_DELETED = 0 AND STORE_BRANCH.IS_DELETED = 0";
        preparedStatement = connection.prepareStatement(sql);
        resultSet = preparedStatement.executeQuery();
        List<Inventory> inventoryList = new ArrayList<>();
        while (resultSet.next()) {
            Product product = Product
                    .builder()
                    .id(resultSet.getInt("productsId"))
                    .name(resultSet.getString("productName"))
                    .build();
            StoreBranch storeBranch = StoreBranch
                    .builder()
                    .id(resultSet.getInt("storeBranchId"))
                    .branchName(resultSet.getString("branch_name"))
                    .build();
            Inventory inventory = Inventory
                    .builder()
                    .id(resultSet.getInt("inventoryId"))
                    .quantity(resultSet.getInt("quantity"))
                    .storeBranch(storeBranch)
                    .product(product)
                    .build();
            inventoryList.add(inventory);
        }
        return inventoryList;
    }

    @Override
    public Inventory findById(Integer id) throws Exception {
        return null;
    }

    public Inventory findById(Integer storeBranchId, Integer productId) throws Exception {
        String sql = "SELECT INVENTORY.ID AS inventoryId, QUANTITY, " +
                "PRODUCTS.ID AS productsId, PRODUCTS.NAME AS productName, " +
                "STORE_BRANCH.ID AS storeBranchId, STORE_BRANCH.BRANCH_NAME " +
                "FROM INVENTORY " +
                "JOIN PRODUCTS ON INVENTORY.PRODUCT_ID = PRODUCTS.ID " +
                "JOIN STORE_BRANCH ON INVENTORY.BRANCH_ID = STORE_BRANCH.ID " +
                "WHERE PRODUCTS.IS_DELETED = 0 AND STORE_BRANCH.IS_DELETED = 0 " +
                "AND STORE_BRANCH.ID = ? AND PRODUCTS.ID=?";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, storeBranchId);
        preparedStatement.setInt(2, productId);
        resultSet = preparedStatement.executeQuery();

        Inventory inventory = null;
        if (resultSet.next()) {
            Product product = Product
                    .builder()
                    .id(resultSet.getInt("productsId"))
                    .name(resultSet.getString("productName"))
                    .build();
            StoreBranch storeBranch = StoreBranch
                    .builder()
                    .id(resultSet.getInt("storeBranchId"))
                    .branchName(resultSet.getString("branch_name"))
                    .build();
            inventory = Inventory
                    .builder()
                    .id(resultSet.getInt("inventoryId"))
                    .quantity(resultSet.getInt("quantity"))
                    .storeBranch(storeBranch)
                    .product(product)
                    .build();
        }
        return inventory;
    }

    public List<Inventory> findAll(String productName, String branchName) throws Exception {
        String sql = "SELECT INVENTORY.ID AS inventoryId, QUANTITY, " +
                "PRODUCTS.ID AS productsId, PRODUCTS.NAME AS productName, " +
                "STORE_BRANCH.ID AS storeBranchId, STORE_BRANCH.BRANCH_NAME " +
                "FROM INVENTORY " +
                "JOIN PRODUCTS ON INVENTORY.PRODUCT_ID = PRODUCTS.ID " +
                "JOIN STORE_BRANCH ON INVENTORY.BRANCH_ID = STORE_BRANCH.ID " +
                "WHERE PRODUCTS.IS_DELETED = 0 AND STORE_BRANCH.IS_DELETED = 0";
        if (productName != null) {
            sql += " AND PRODUCTS.NAME LIKE '%" + productName + "%'";
            preparedStatement.setString(1, productName);
        }
        if (branchName != null) {
            sql += " AND BRANCH_NAME LIKE '%" + branchName + "%'";
            preparedStatement.setString(1, branchName);
        }
        if (productName != null && branchName != null) {
            sql += " AND PRODUCTS.NAME LIKE '%" + productName + "%'" +
                    " AND BRANCH_NAME LIKE '%" + branchName + "%'";
            preparedStatement.setString(1, productName);
            preparedStatement.setString(2, branchName);
        }
        preparedStatement = connection.prepareStatement(sql);
        resultSet = preparedStatement.executeQuery();
        List<Inventory> inventoryList = new ArrayList<>();
        while (resultSet.next()) {
            Product product = Product
                    .builder()
                    .id(resultSet.getInt("productsId"))
                    .name(resultSet.getString("productName"))
                    .build();
            StoreBranch storeBranch = StoreBranch
                    .builder()
                    .id(resultSet.getInt("storeBranchId"))
                    .branchName(resultSet.getString("branch_name"))
                    .build();
            Inventory inventory = Inventory
                    .builder()
                    .id(resultSet.getInt("inventoryId"))
                    .quantity(resultSet.getInt("quantity"))
                    .storeBranch(storeBranch)
                    .product(product)
                    .build();
            inventoryList.add(inventory);
        }
        return inventoryList;
    }

    public void calcBalance(int newQuantity, Inventory inventory) throws Exception {
        preparedStatement = connection.prepareStatement(
                "UPDATE INVENTORY " +
                        "SET QUANTITY=? " +
                        "WHERE ID=? AND PRODUCT_ID=? AND BRANCH_ID=?"
        );
        preparedStatement.setInt(1, newQuantity);
        preparedStatement.setInt(2, inventory.getId());
        preparedStatement.setInt(3, inventory.getProduct().getId());
        preparedStatement.setInt(4, inventory.getStoreBranch().getId());
        preparedStatement.execute();
    }

    @Override
    public void close() throws Exception {
        preparedStatement.close();
        connection.close();
        log.info("InventoryRepo Connection closed");
    }
}
