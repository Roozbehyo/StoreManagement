package com.storemgmt.Model.Repo;

import com.storemgmt.Model.Db.ConnectionProvider;
import com.storemgmt.Model.Entity.StoreBranch;
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
                "INSERT INTO STORE_BRANCH(ID, BRANCH_NAME)" +
                        " VALUES (?,?)"
        );
        preparedStatement.setInt(1, storeBranch.getId());
        preparedStatement.setString(2, storeBranch.getBranchName());
        preparedStatement.execute();
    }

    @Override
    public void edit(StoreBranch storeBranch) throws Exception {
        preparedStatement = connection.prepareStatement(
                "UPDATE STORE_BRANCH " +
                        "SET BRANCH_NAME=? " +
                        "WHERE ID=? "
        );
        preparedStatement.setString(1, storeBranch.getBranchName());
        preparedStatement.setInt(2, storeBranch.getId());
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
        String sql = "SELECT STORE_BRANCH.ID, BRANCH_NAME " +
                "FROM STORE_BRANCH " +
                "WHERE STORE_BRANCH.IS_DELETED=0";
        preparedStatement = connection.prepareStatement(sql);
        resultSet = preparedStatement.executeQuery();
        List<StoreBranch> storeBranchList = new ArrayList<>();
        while (resultSet.next()) {
            StoreBranch storeBranch = StoreBranch
                    .builder()
                    .id(resultSet.getInt("id"))
                    .branchName(resultSet.getString("branch_name"))
                    .build();
            storeBranchList.add(storeBranch);
        }
        return storeBranchList;
    }

    @Override
    public StoreBranch findById(Integer id) throws Exception {
        String sql = "SELECT STORE_BRANCH.ID, BRANCH_NAME " +
                "FROM STORE_BRANCH " +
                "WHERE STORE_BRANCH.IS_DELETED=0 AND STORE_BRANCH.ID = ?";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        resultSet = preparedStatement.executeQuery();

        StoreBranch storeBranch = null;
        if (resultSet.next()) {
            storeBranch = StoreBranch
                    .builder()
                    .id(resultSet.getInt("id"))
                    .branchName(resultSet.getString("branch_name"))
                    .build();
        }
        return storeBranch;
    }

    public List<StoreBranch> findByName(String name) throws Exception {
        String sql = "SELECT STORE_BRANCH.ID, BRANCH_NAME " +
                "FROM STORE_BRANCH " +
                "WHERE STORE_BRANCH.IS_DELETED=0 AND BRANCH_NAME LIKE ?";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, name + "%");
        resultSet = preparedStatement.executeQuery();
        List<StoreBranch> storeBranchList = new ArrayList<>();
        while (resultSet.next()) {
            StoreBranch storeBranch = StoreBranch
                    .builder()
                    .id(resultSet.getInt("id"))
                    .branchName(resultSet.getString("branch_name"))
                    .build();
            storeBranchList.add(storeBranch);
        }
        return storeBranchList;
    }

    @Override
    public void close() throws Exception {
        preparedStatement.close();
        connection.close();
        log.info("storeBranchRepo Connection closed");
    }
}
