package com.storemgmt.Controller;

import com.storemgmt.Model.Entity.Enum.ViewFormType;
import com.storemgmt.Model.Entity.Product;
import com.storemgmt.Model.Service.ProductService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

import java.util.List;

@Log4j
public class ProductModalController {

    @FXML
    private TableView<Product> productTable;
    @FXML
    private TableColumn<Product, String> nameCol, priceCol;
    @FXML
    private Button selectBtn, cancelBtn;

    @Setter
    private ViewFormType viewFormType;
    private Product selectedproduct;
    ProductService productService = new ProductService();
    @Setter
    OrderItemController orderItemController;
    @Setter
    InventoryController inventoryController;

    @FXML
    public void initialize() {
        try {
            loadProductData();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Nothing to show");
            alert.show();
            log.error(e);
        }

        selectBtn.setOnAction(e -> {
            selectedproduct = productTable.getSelectionModel().getSelectedItem();
            if (selectedproduct != null) {
                Stage stage = (Stage) selectBtn.getScene().getWindow();
                stage.close();
                if (viewFormType == ViewFormType.Order_Item){
                    orderItemController.fillProductField(selectedproduct);
                }
                if (viewFormType == ViewFormType.Inventory){
                    inventoryController.fillProductField(selectedproduct);
                }
            }
        });
        cancelBtn.setOnAction(e -> {
            Stage stage = (Stage) selectBtn.getScene().getWindow();
            stage.close();
        });
    }

    public void loadProductData() throws Exception {
        try {
            List<Product> productList = productService.findAll();
            ObservableList<Product> productObservableList = FXCollections.observableArrayList(productList);

            nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
            productTable.setItems(productObservableList);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
