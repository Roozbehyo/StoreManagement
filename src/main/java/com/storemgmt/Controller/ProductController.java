package com.storemgmt.Controller;

import com.storemgmt.Model.Entity.Enum.FormState;
import com.storemgmt.Model.Entity.Product;
import com.storemgmt.Model.Service.ProductService;
import com.storemgmt.Model.Validation.ProductValidator;
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
public class ProductController implements Initializable {
    @FXML
    private TextField id, name, price, nameSearchTxt;

    @FXML
    private Button saveBtn, updateBtn, deleteBtn, refreshBtn;

    @FXML
    private TableView<Product> productTable;

    @FXML
    private TableColumn<Product, Integer> idCol;

    @FXML
    private TableColumn<Product, String> nameCol, priceCol;

    private final ProductValidator productValidator = new ProductValidator();
    private final ProductService productService = new ProductService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (FormViewer.productFormState.equals(FormState.New)) {
            updateBtn.setDisable(true);
            deleteBtn.setDisable(true);
            refreshBtn.setVisible(false);
            log.info("Save Mode");
        }
        if (FormViewer.productFormState.equals(FormState.Update)) {
            saveBtn.setDisable(true);
            deleteBtn.setDisable(true);
            fillFieldsByMouseClick();
            log.info("Update Mode");
        }
        if (FormViewer.productFormState.equals(FormState.Remove)) {
            saveBtn.setDisable(true);
            updateBtn.setDisable(true);
            fillFieldsByMouseClick();
            log.info("Remove Mode");
        }
        log.info("View initialized");
        resetForm();

        saveBtn.setOnAction(event -> {
            try {
                Product product = Product
                        .builder()
                        .name(name.getText())
                        .price(Float.valueOf(price.getText()))
                        .build();

                productValidator.validateProduct(product);
                try {
                    productService.save(product);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "New Product Saved", ButtonType.OK);
                    alert.show();
                    resetForm();
                    log.info("Product Saved : " + product);
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR,
                            "Saving Product Failed\n" + e.getMessage(), ButtonType.OK);
                    alert.show();
                    log.error("productService.save: " + e);
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
        updateBtn.setOnAction(event -> {
            try {
                Product product = Product
                        .builder()
                        .name(name.getText())
                        .price(Float.valueOf(price.getText()))
                        .build();

                productValidator.validateProduct(product);
                try {
                    productService.edit(product);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Product Edited", ButtonType.OK);
                    alert.show();
                    resetForm();
                    log.info("Product Edited : " + product);
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR,
                            "Editing Product Failed\n" + e.getMessage(), ButtonType.OK);
                    alert.show();
                    log.error("productService.edit: " + e);
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
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you wanna delete?",
                    ButtonType.YES, ButtonType.NO);
            confirmation.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    try {
                        productService.remove(Integer.parseInt(id.getText()));
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Product Deleted", ButtonType.OK);
                        alert.show();
                        resetForm();
                        log.info("Product Deleted");
                    } catch (Exception e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Deleting Process Failed");
                        alert.show();
                        log.error(e);
                    }
                }
            });
        });

        refreshBtn.setOnAction(event -> {
            resetForm();
        });

        nameSearchTxt.setOnKeyReleased(event -> {
            if (nameSearchTxt.getLength() > 0) {
                try {
                    refreshTable(productService.findAllByName(nameSearchTxt.getText()));
                } catch (Exception e) {
                    log.error(e);
                }
            }
        });
    }

    private void resetForm() {
        id.clear();
        name.clear();
        price.clear();
        nameSearchTxt.clear();
        try {
            refreshTable(productService.findAll());
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No Product Found", ButtonType.OK);
            alert.show();
            log.error(e);
        }
    }

    private void refreshTable(List<Product> productList) {
        ObservableList<Product> productObservableList = FXCollections.observableArrayList(productList);
        productTable.getItems().clear();

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        productTable.setItems(productObservableList);
    }

    private void fillFieldsByMouseClick() {
        productTable.setOnMouseReleased(event -> {
            Product product = productTable.getSelectionModel().getSelectedItem();
            if (product != null) {
                id.setText(String.valueOf(product.getId()));
                name.setText(product.getName());
                price.setText(product.getPrice().toString());
            }
        });
    }
}
