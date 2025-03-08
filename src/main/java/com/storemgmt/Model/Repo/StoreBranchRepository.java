package com.storemgmt.Model.Repo;

import com.storemgmt.Model.ConnectionProvider;
import com.storemgmt.Model.Entity.StoreBranch;
import com.storemgmt.Model.Entity.Seller;
import lombok.extern.log4j.Log4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Log4j
public class StoreBranchRepository implements Repository<StoreBranch, Integer> {
    private final Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public StoreBranchRepository() throws Exception {
        connection = ConnectionProvider.getConnectionProvider().getConnection();
        log.info("Connected to database");
    }

    @Override
    public void save(StoreBranch storeBranch) throws Exception {
        storeBranch.setId(ConnectionProvider.getConnectionProvider().nextId("store_branch_seq"));
        preparedStatement = connection.prepareStatement(
                "INSERT INTO STORE_BRANCH(ID, BRANCH_NAME, SELLER_ID)" +
                        " VALUES (?,?,?)"
        );
        preparedStatement.setInt(1, storeBranch.getId());
        preparedStatement.setString(2, storeBranch.getBranchName());
        preparedStatement.setInt(3, storeBranch.getSeller().getId());
        preparedStatement.execute();
    }

    @Override
    public void edit(StoreBranch storeBranch) throws Exception {
        preparedStatement = connection.prepareStatement(
                "UPDATE STORE_BRANCH " +
                        "SET BRANCH_NAME=?, SELLER_ID=? " +
                        "WHERE ID=? "
        );
        preparedStatement.setString(1, storeBranch.getBranchName());
        preparedStatement.setInt(2, storeBranch.getSeller().getId());
        preparedStatement.setInt(3, storeBranch.getId());
        preparedStatement.execute();
    }

    @Override
    public void remove(Integer id) throws Exception {
        preparedStatement = connection.prepareStatement(
                "UPDATE STORE_BRANCH " +
                        "SET IS_DELETED=1 " +
                        "WHERE ID=? "
        );
        preparedStatement.setInt(1, id);
        preparedStatement.execute();
    }

    @Override
    public List<StoreBranch> findAll() throws Exception {
        String sql = "SELECT STORE_BRANCH.ID AS storeBranchId, BRANCH_NAME, " +
                "SELLERS.ID AS sellerId, SELLERS.FIRSTNAME AS sellerFName, " +
                "SELLERS.LASTNAME AS sellerLName " +
                "FROM STORE_BRANCH " +
                "JOIN SELLERS ON STORE_BRANCH.SELLER_ID = SELLERS.ID " +
                "WHERE STORE_BRANCH.IS_DELETED=0";
        preparedStatement = connection.prepareStatement(sql);
        resultSet = preparedStatement.executeQuery();
        List<StoreBranch> storeBranchList = new ArrayList<>();
        while (resultSet.next()) {
            Seller seller = Seller
                    .builder()
                    .id(resultSet.getInt("sellersId"))
                    .firstname(resultSet.getString("sellerFName"))
                    .lastname(resultSet.getString("sellerLName"))
                    .build();
            StoreBranch storeBranch = StoreBranch
                    .builder()
                    .id(resultSet.getInt("storeBranchId"))
                    .branchName(resultSet.getString("branch_name"))
                    .seller(seller)
                    .build();
            storeBranchList.add(storeBranch);
        }
        return storeBranchList;
    }

    @Override
    public StoreBranch findById(Integer id) throws Exception {
        String sql = "SELECT STORE_BRANCH.ID AS storeBranchId, BRANCH_NAME, " +
                "SELLERS.ID AS sellerId, SELLERS.FIRSTNAME AS sellerFName, " +
                "SELLERS.LASTNAME AS sellerLName " +
                "FROM STORE_BRANCH " +
                "JOIN SELLERS ON STORE_BRANCH.SELLER_ID = SELLERS.ID " +
                "WHERE STORE_BRANCH.IS_DELETED=0 AND STORE_BRANCH.ID = ?";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        resultSet = preparedStatement.executeQuery();

        StoreBranch storeBranch = null;
        if (resultSet.next()) {
            Seller seller = Seller
                    .builder()
                    .id(resultSet.getInt("sellersId"))
                    .firstname(resultSet.getString("sellerFName"))
                    .lastname(resultSet.getString("sellerLName"))
                    .build();
            storeBranch = StoreBranch
                    .builder()
                    .id(resultSet.getInt("storeBranchId"))
                    .branchName(resultSet.getString("branch_name"))
                    .seller(seller)
                    .build();
        }
        return storeBranch;
    }

    @Override
    public void close() throws Exception {
        preparedStatement.close();
        connection.close();
        log.info("storeBranchRepo Connection closed");
    }
}
