package com.storemgmt.common.Service;

import com.storemgmt.common.Entity.*;
import com.storemgmt.common.Repo.InventoryRepository;

import java.util.List;

public class InventoryService implements Service<Inventory, Integer> {
    @Override
    public void save(Inventory inventory) throws Exception {
        validate(inventory);
        try (InventoryRepository inventoryRepository = new InventoryRepository()) {
            Inventory result =
                    inventoryRepository.findById(inventory.getStoreBranch().getId(), inventory.getProduct().getId());
            if (result != null) {
                throw new Exception("The Item Is Already Exists");
            }
            inventoryRepository.save(inventory);
        }
    }

    @Override
    public void edit(Inventory inventory) throws Exception {
        validate(inventory);
        findById(inventory.getStoreBranch().getId(), inventory.getProduct().getId());
        try (InventoryRepository inventoryRepository = new InventoryRepository()) {
            inventoryRepository.edit(inventory);
        }
    }

    @Override
    public void remove(Integer branchId) throws Exception {
        StoreBranch storeBranch = new StoreBranchService().findById(branchId);
        if (storeBranch == null) {
            throw new Exception("Store branch not found");
        }
        try (InventoryRepository inventoryRepository = new InventoryRepository()) {
            inventoryRepository.remove(branchId);
        }
    }

    public void remove(int branchId, int productId) throws Exception {
        StoreBranch storeBranch = new StoreBranchService().findById(branchId);
        if (storeBranch == null) {
            throw new Exception("Store branch not found");
        }
        Product product = new ProductService().findById(productId);
        if (product == null) {
            throw new Exception("Product Not Found");
        }
        try (InventoryRepository inventoryRepository = new InventoryRepository()) {
            inventoryRepository.remove(branchId, productId);
        }
    }

    @Override
    public List<Inventory> findAll() throws Exception {
        try (InventoryRepository inventoryRepository = new InventoryRepository()) {
            List<Inventory> inventoryList = inventoryRepository.findAll();
            if (inventoryList.isEmpty()) {
                throw new Exception("Nothing Found");
            }
            return inventoryList;
        }
    }

    public List<Inventory> findAll(String productName, String branchName) throws Exception {
        try (InventoryRepository inventoryRepository = new InventoryRepository()) {
            List<Inventory> inventoryList = inventoryRepository.findAll(productName, branchName);
            if (inventoryList.isEmpty()) {
                throw new Exception("Nothing Found");
            }
            return inventoryList;
        }
    }

    public List<Inventory> findAllByProductName(String productName) throws Exception {
        try (InventoryRepository inventoryRepository = new InventoryRepository()) {
            List<Inventory> inventoryList = inventoryRepository.findAll(productName, null);
            if (inventoryList.isEmpty()) {
                throw new Exception("Nothing Found");
            }
            return inventoryList;
        }
    }

    public List<Inventory> findAllByBranchName(String branchName) throws Exception {
        try (InventoryRepository inventoryRepository = new InventoryRepository()) {
            List<Inventory> inventoryList = inventoryRepository.findAll(null, branchName);
            if (inventoryList.isEmpty()) {
                throw new Exception("Nothing Found");
            }
            return inventoryList;
        }
    }

    @Override
    public Inventory findById(Integer id) throws Exception {
        return null;
    }

    public Inventory findById(Integer storeBranchId, Integer productId) throws Exception {
        try (InventoryRepository inventoryRepository = new InventoryRepository()) {
            Inventory inventory = inventoryRepository.findById(storeBranchId, productId);
            if (inventory == null) {
                throw new Exception("Nothing Found");
            }
            return inventory;
        }
    }

    public void validate(Inventory inventory) throws Exception {
        StoreBranch storeBranch = new StoreBranchService().findById(inventory.getStoreBranch().getId());
        if (storeBranch == null) {
            throw new Exception("Store branch not found");
        }
        Product product = new ProductService().findById(inventory.getProduct().getId());
        if (product == null) {
            throw new Exception("Product Not Found");
        }
        if (inventory.getQuantity() < 0) {
            throw new Exception("Quantity cannot be negative");
        }
    }

    public void calcBalance(Order order, OrderItem orderItem) throws Exception {
        StoreBranch storeBranch = new StoreBranchService().findById(order.getStoreBranch().getId());
        if (storeBranch == null) {
            throw new Exception("Store branch not found");
        }
        Inventory inventory = findById(storeBranch.getId(), orderItem.getProduct().getId());
        if (inventory == null) {
            throw new Exception("Inventory not found");
        }
        if (inventory.getQuantity() < orderItem.getQuantity()) {
            throw new Exception("Quantity cannot be negative");
        }
        try (InventoryRepository inventoryRepository = new InventoryRepository()) {
            int minus = inventory.getQuantity() - orderItem.getQuantity();
            inventoryRepository.calcBalance(minus, inventory);
        }
    }
}