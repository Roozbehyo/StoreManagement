package com.storemgmt.Model.Repo;

import com.storemgmt.Model.ConnectionProvider;
import com.storemgmt.Model.Entity.Inventory;
import com.storemgmt.Model.Entity.Product;
import com.storemgmt.Model.Entity.StoreBranch;
import lombok.extern.log4j.Log4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
                        "WHERE ID=? "
        );
        preparedStatement.setInt(1, inventory.getStoreBranch().getId());
        preparedStatement.setInt(2, inventory.getQuantity());
        preparedStatement.setInt(3, inventory.getId());
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
                "JOIN STORE_BRANCH ON INVENTORY.BRANCH_ID = STORE_BRANCH.ID "+
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
        String sql = "SELECT INVENTORY.ID AS inventoryId, QUANTITY, " +
                "PRODUCTS.ID AS productsId, PRODUCTS.NAME AS productName, " +
                "STORE_BRANCH.ID AS storeBranchId, STORE_BRANCH.BRANCH_NAME " +
                "FROM INVENTORY " +
                "JOIN PRODUCTS ON INVENTORY.PRODUCT_ID = PRODUCTS.ID " +
                "JOIN STORE_BRANCH ON INVENTORY.BRANCH_ID = STORE_BRANCH.ID "+
                "WHERE PRODUCTS.IS_DELETED = 0 AND STORE_BRANCH.IS_DELETED = 0 " +
                "AND STORE_BRANCH.ID = ?";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
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

    @Override
    public void close() throws Exception {
        preparedStatement.close();
        connection.close();
        log.info("inventoryRepo Connection closed");
    }
}
