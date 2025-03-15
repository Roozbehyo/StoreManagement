package com.storemgmt.Controller;

import com.storemgmt.Model.Entity.Enum.FormState;
import com.storemgmt.Model.Entity.Enum.ViewFormType;
import com.storemgmt.Model.Entity.Inventory;
import com.storemgmt.Model.Entity.Product;
import com.storemgmt.Model.Entity.StoreBranch;
import com.storemgmt.Model.Service.InventoryService;
import com.storemgmt.Model.Validation.InventoryValidator;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.extern.log4j.Log4j;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Log4j
public class InventoryController implements Initializable {
    @FXML
    private TextField product, quantity, branch, productId, branchId;
    @FXML
    private Button saveBtn, updateBtn, deleteBtn, refreshBtn, addProductBtn, addBranchBtn;
    @FXML
    private TableView<Inventory> inventoryTable;
    @FXML
    private TableColumn<Inventory, String> productCol, quantityCol, branchCol;

    private InventoryValidator inventoryValidator;
    private InventoryService inventoryService;
    private FormViewer formViewer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (FormViewer.inventoryFormState.equals(FormState.New)) {
            updateBtn.setDisable(true);
            deleteBtn.setDisable(true);
            refreshBtn.setVisible(false);
            log.info("Save Mode");
        }
        if (FormViewer.inventoryFormState.equals(FormState.Update)) {
            addProductBtn.setDisable(true);
            addBranchBtn.setDisable(true);
            saveBtn.setDisable(true);
            deleteBtn.setDisable(true);
            fillFieldsByMouseClick();
            log.info("Update Mode");
        }
        if (FormViewer.inventoryFormState.equals(FormState.Remove)) {
            addProductBtn.setDisable(true);
            addBranchBtn.setDisable(true);
            quantity.setDisable(true);
            saveBtn.setDisable(true);
            updateBtn.setDisable(true);
            fillFieldsByMouseClick();
            log.info("Remove Mode");
        }
        log.info("View initialized");
        resetForm();

        addProductBtn.setOnAction(event -> {
            try {
                formViewer.showForm("productModal", "Product");
                ProductModalController productModalController = new ProductModalController();
                productModalController.setViewFormType(ViewFormType.Inventory);
                productModalController.loadProductData();
            } catch (Exception e) {
                log.error(e);
            }
        });

        addBranchBtn.setOnAction(event -> {
            try {
                formViewer.showForm("branchModal", "Branch");
                BranchModalController branchModalController = new BranchModalController();
                branchModalController.setViewFormType(ViewFormType.Inventory);
                branchModalController.loadBranchData();
            } catch (Exception e) {
                log.error(e);
            }
        });

        saveBtn.setOnAction(event -> {
            try {
                Inventory inventory = Inventory
                        .builder()
                        .product(Product.builder().id(Integer.parseInt(productId.getText())).build())
                        .storeBranch(StoreBranch.builder().id(Integer.parseInt(branchId.getText())).build())
                        .quantity(Integer.parseInt(quantity.getText()))
                        .build();

                inventoryValidator.validateInventory(inventory);
                try {
                    inventoryService.save(inventory);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "New Inventory Saved", ButtonType.OK);
                    alert.show();
                    resetForm();
                    log.info("inventory Saved : " + inventory);
                    formViewer.showForm("inventory", "Inventory Detail");
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Saving Inventory Failed", ButtonType.OK);
                    alert.show();
                    log.error("inventoryService.save:" + e);
                }
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
                alert.show();
                log.error(e);
            }
        });
        updateBtn.setOnAction(event -> {
            try {
                Inventory inventory = Inventory
                        .builder()
                        .product(Product.builder().id(Integer.parseInt(productId.getText())).build())
                        .storeBranch(StoreBranch.builder().id(Integer.parseInt(branchId.getText())).build())
                        .quantity(Integer.parseInt(quantity.getText()))
                        .build();

                inventoryValidator.validateInventory(inventory);
                try {
                    inventoryService.edit(inventory);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Inventory Edited", ButtonType.OK);
                    alert.show();
                    resetForm();
                    log.info("Inventory Edited : " + inventory);
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Editing Process Failed");
                    alert.show();
                    log.error("Error in inventoryService.edit: ", e);
                }
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
                alert.show();
                log.error(e);
            }
        });

        deleteBtn.setOnAction(event -> {
            try {
                Inventory inventory = Inventory
                        .builder()
                        .product(Product.builder().id(Integer.parseInt(productId.getText())).build())
                        .storeBranch(StoreBranch.builder().id(Integer.parseInt(branchId.getText())).build())
                        .build();

                inventoryValidator.validateInventory(inventory);
                try {
                    inventoryService.remove(Integer.parseInt(branchId.getText()),Integer.parseInt(productId.getText()));
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Inventory Edited", ButtonType.OK);
                    alert.show();
                    resetForm();
                    log.info("inventory Edited : " + inventory);
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Editing Process Failed");
                    alert.show();
                    log.error("Error in inventoryService.edit: ", e);
                }
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
                alert.show();
                log.error(e);
            }
        });

        refreshBtn.setOnAction(event -> {
            resetForm();
        });
    }

    private void resetForm() {
        product.clear();
        quantity.clear();
        branch.clear();
        try {
            refreshTable(inventoryService.findAll());
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No Inventory Found", ButtonType.OK);
            alert.show();
            log.error(e);
        }
    }

    private void refreshTable(List<Inventory> inventoryList) {
        ObservableList<Inventory> inventoryObservableList = FXCollections.observableArrayList(inventoryList);
        inventoryTable.getItems().clear();

        productCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue()
                .getProduct().getName()));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        branchCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue()
                .getStoreBranch().getBranchName()));
        inventoryTable.setItems(inventoryObservableList);
    }

    private void fillFieldsByMouseClick() {
        inventoryTable.setOnMouseReleased(event -> {
            Inventory inventory = inventoryTable.getSelectionModel().getSelectedItem();
            if (inventory != null) {
                product.setText(inventory.getProduct().getName());
                quantity.setText(String.valueOf(inventory.getQuantity()));
                branch.setText(inventory.getStoreBranch().getBranchName());
            }
        });
    }

    public void fillProductField(Product selectedProduct) {
        System.out.println(selectedProduct);
        System.out.println(selectedProduct.getName());
        System.out.println("Trying to set text...");
        Platform.runLater(() -> {
            if (product != null) {
                product.setText("Test Value");
                System.out.println("Text Set Successfully!");
            } else {
                System.out.println("product is null!");
            }
        });
        product.setText(selectedProduct.getName());
        productId.setText(String.valueOf(selectedProduct.getId()));
    }

    public void fillBranchField(StoreBranch selectedBranch) {
        System.out.println(selectedBranch);
        System.out.println(selectedBranch.getBranchName());
        System.out.println("Trying to set text...");
        Platform.runLater(() -> {
            if (product != null) {
                product.setText("Test Value");
                System.out.println("Text Set Successfully!");
            } else {
                System.out.println("Store Branch is null!");
            }
        });
        product.setText(selectedBranch.getBranchName());
        productId.setText(String.valueOf(selectedBranch.getId()));
    }
}