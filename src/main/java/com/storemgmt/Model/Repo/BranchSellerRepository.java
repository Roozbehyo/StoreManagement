package com.storemgmt.Model.Repo;

import com.storemgmt.Model.Db.ConnectionProvider;
import com.storemgmt.Model.Entity.Seller;
import com.storemgmt.Model.Entity.BranchSeller;
import com.storemgmt.Model.Entity.StoreBranch;
import lombok.extern.log4j.Log4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Log4j
public class BranchSellerRepository implements Repository<BranchSeller, Integer> {
    private final Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public BranchSellerRepository() throws Exception {
        connection = ConnectionProvider.getConnectionProvider().getConnection();
        log.info("Connected to database");
    }

    @Override
    public void save(BranchSeller branchSeller) throws Exception {
        branchSeller.setId(ConnectionProvider.getConnectionProvider().nextId("branch_seller_seq"));
        preparedStatement = connection.prepareStatement(
                "INSERT INTO BRANCH_SELLER(ID, BRANCH_ID, SELLER_ID)" +
                        " VALUES (?,?,?)"
        );
        preparedStatement.setInt(1, branchSeller.getId());
        preparedStatement.setInt(2, branchSeller.getStoreBranch().getId());
        preparedStatement.setInt(3, branchSeller.getSeller().getId());
        preparedStatement.execute();
    }

    @Override
    public void edit(BranchSeller branchSeller) throws Exception {
        preparedStatement = connection.prepareStatement(
                "UPDATE BRANCH_SELLER " +
                        "SET SELLER_ID=? " +
                        "WHERE BRANCH_ID=?"
        );
        preparedStatement.setInt(1, branchSeller.getSeller().getId());
        preparedStatement.setInt(2, branchSeller.getStoreBranch().getId());
        preparedStatement.execute();
    }

    @Override
    public void remove(Integer storeBranchId) throws Exception {
        preparedStatement = connection.prepareStatement(
                "DELETE FROM BRANCH_SELLER " +
                        "WHERE BRANCH_ID=? "
        );
        preparedStatement.setInt(1, storeBranchId);
        preparedStatement.execute();
    }

    public void remove(int storeBranchId, int sellerId) throws Exception {
        preparedStatement = connection.prepareStatement(
                "DELETE FROM BRANCH_SELLER " +
                        "WHERE SELLER_ID=? AND BRANCH_ID=? "
        );
        preparedStatement.setInt(1, sellerId);
        preparedStatement.setInt(2, storeBranchId);
        preparedStatement.execute();
    }

    @Override
    public List<BranchSeller> findAll() throws Exception {
        return null;
    }

    @Override
    public BranchSeller findById(Integer sellerId) throws Exception {
        String sql = "SELECT STORE_BRANCH.BRANCH_NAME, " +
                "SELLERS.ID AS sellerId, SELLERS.FIRSTNAME AS sellerFName, " +
                "SELLERS.LASTNAME AS sellerLName " +
                "FROM BRANCH_SELLER " +
                "JOIN STORE_BRANCH ON BRANCH_SELLER.BRANCH_ID = STORE_BRANCH.ID " +
                "JOIN SELLERS ON BRANCH_SELLER.SELLER_ID = SELLERS.ID " +
                "WHERE SELLER_ID=?";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, sellerId);
        resultSet = preparedStatement.executeQuery();
        BranchSeller branchSeller = null;
        if (resultSet.next()) {
            StoreBranch storeBranch = StoreBranch
                    .builder()
                    .branchName(resultSet.getString("BRANCH_NAME"))
                    .build();
            Seller seller = Seller
                    .builder()
                    .id(resultSet.getInt("sellerId"))
                    .firstname(resultSet.getString("sellerFName"))
                    .lastname(resultSet.getString("sellerLName"))
                    .build();
            branchSeller = BranchSeller
                    .builder()
                    .storeBranch(storeBranch)
                    .seller(seller)
                    .build();
        }
        return branchSeller;
    }

    public BranchSeller findById(int storeBranchId, int sellerId) throws Exception {
        String sql = "SELECT STORE_BRANCH.BRANCH_NAME, " +
                "SELLERS.ID AS sellerId, SELLERS.FIRSTNAME AS sellerFName, " +
                "SELLERS.LASTNAME AS sellerLName " +
                "FROM BRANCH_SELLER " +
                "JOIN STORE_BRANCH ON BRANCH_SELLER.BRANCH_ID = STORE_BRANCH.ID " +
                "JOIN SELLERS ON BRANCH_SELLER.SELLER_ID = SELLERS.ID " +
                "WHERE BRANCH_ID=? AND SELLER_ID=?";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, storeBranchId);
        preparedStatement.setInt(2, sellerId);
        resultSet = preparedStatement.executeQuery();
        BranchSeller branchSeller = null;
        if (resultSet.next()) {
            StoreBranch storeBranch = StoreBranch
                    .builder()
                    .branchName(resultSet.getString("BRANCH_NAME"))
                    .build();
            Seller seller = Seller
                    .builder()
                    .id(resultSet.getInt("sellerId"))
                    .firstname(resultSet.getString("sellerFName"))
                    .lastname(resultSet.getString("sellerLName"))
                    .build();
            branchSeller = BranchSeller
                    .builder()
                    .storeBranch(storeBranch)
                    .seller(seller)
                    .build();
        }
        return branchSeller;
    }

    public List<BranchSeller> findAllByStoreBranchId(int storeBranchId) throws Exception {
        String sql = "SELECT STORE_BRANCH.BRANCH_NAME, " +
                "SELLERS.ID AS sellerId, SELLERS.FIRSTNAME AS sellerFName, " +
                "SELLERS.LASTNAME AS sellerLName " +
                "FROM BRANCH_SELLER " +
                "JOIN STORE_BRANCH ON BRANCH_SELLER.BRANCH_ID = STORE_BRANCH.ID " +
                "JOIN SELLERS ON BRANCH_SELLER.SELLER_ID = SELLERS.ID " +
                "WHERE BRANCH_ID=?";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, storeBranchId);
        resultSet = preparedStatement.executeQuery();
        List<BranchSeller> branchSellerList = new ArrayList<>();
        while (resultSet.next()) {
            StoreBranch storeBranch = StoreBranch
                    .builder()
                    .branchName(resultSet.getString("BRANCH_NAME"))
                    .build();
            Seller seller = Seller
                    .builder()
                    .id(resultSet.getInt("sellerId"))
                    .firstname(resultSet.getString("sellerFName"))
                    .lastname(resultSet.getString("sellerLName"))
                    .build();
            BranchSeller branchSeller = BranchSeller
                    .builder()
                    .storeBranch(storeBranch)
                    .seller(seller)
                    .build();
            branchSellerList.add(branchSeller);
        }
        return branchSellerList;
    }

    @Override
    public void close() throws Exception {
        preparedStatement.close();
        connection.close();
        log.info("branchSellerRepo Connection closed");
    }
}
