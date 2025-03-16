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

    private final InventoryValidator inventoryValidator = new InventoryValidator();
    private final InventoryService inventoryService = new InventoryService();
    private final FormViewer formViewer = new FormViewer();

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
                ProductModalController productModalController =
                        (ProductModalController) FormViewer.getLastLoadedController();
                productModalController.setViewFormType(ViewFormType.Inventory);
                productModalController.setInventoryController(this);
            } catch (Exception e) {
                log.error(e);
            }
        });

        addBranchBtn.setOnAction(event -> {
            try {
                formViewer.showForm("storeBranchModal", "Branch");
                BranchModalController branchModalController =
                        (BranchModalController) FormViewer.getLastLoadedController();
                branchModalController.setViewFormType(ViewFormType.Inventory);
                branchModalController.setInventoryController(this);
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
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "New Item Saved", ButtonType.OK);
                    alert.show();
                    resetForm();
                    log.info("inventory Saved : " + inventory);
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR,
                            "Saving Item Failed\n" + e.getMessage(), ButtonType.OK);
                    alert.show();
                    log.error("inventoryService.save:" + e);
                }
            } catch (NumberFormatException | NullPointerException n) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please Fill All Fields", ButtonType.OK);
                alert.show();
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
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Item Edited", ButtonType.OK);
                    alert.show();
                    resetForm();
                    log.info("Item Edited : " + inventory);
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Editing Process Failed");
                    alert.show();
                    log.error("Error in inventoryService.edit: ", e);
                }
            } catch (NullPointerException nullPointerException) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please Fill All Fields", ButtonType.OK);
                alert.show();
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
                    inventoryService.remove(Integer.parseInt(branchId.getText()), Integer.parseInt(productId.getText()));
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Item Deleted", ButtonType.OK);
                    alert.show();
                    resetForm();
                    log.info("Item Deleted : " + inventory);
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Deleting Process Failed");
                    alert.show();
                    log.error("Error in inventoryService.remove: ", e);
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
        branchId.clear();
        productId.clear();
        try {
            refreshTable(inventoryService.findAll());
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Nothing Found", ButtonType.OK);
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
                productId.setText(String.valueOf(inventory.getProduct().getId()));
                branchId.setText(String.valueOf(inventory.getStoreBranch().getId()));
            }
        });
    }

    public void fillProductField(Product selectedProduct) {
        Platform.runLater(() -> {
            if (product != null) {
                product.setText(selectedProduct.getName());
                productId.setText(String.valueOf(selectedProduct.getId()));
            }
        });
    }

    public void fillBranchField(StoreBranch selectedBranch) {
        Platform.runLater(() -> {
            if (branch != null) {
                branch.setText(selectedBranch.getBranchName());
                branchId.setText(String.valueOf(selectedBranch.getId()));
            }
        });
    }
}