package com.storemgmt.Controller;

import com.storemgmt.Model.Entity.Enum.ViewFormType;
import com.storemgmt.Model.Entity.Order;
import com.storemgmt.Model.Entity.OrderItem;
import com.storemgmt.Model.Entity.Product;
import com.storemgmt.Model.Service.OrderItemService;
import com.storemgmt.Model.Service.OrderService;
import com.storemgmt.Model.Validation.OrderItemValidator;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Log4j
public class OrderItemController implements Initializable {
    @FXML
    private TextField orderIdTxt, product, quantity, productId;
    @FXML
    private Button saveBtn, refreshBtn, addProductBtn;
    @FXML
    private TableView<OrderItem> orderItemTable;
    @FXML
    private TableColumn<OrderItem, String> productNameCol, productPriceCol, quantityCol;

    private final OrderItemValidator orderItemValidator = new OrderItemValidator();
    private final OrderItemService orderItemService = new OrderItemService();
    private final OrderService orderService = new OrderService();
    private final FormViewer formViewer = new FormViewer();
    @Setter
    private Integer orderId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("View initialized");
        addProductBtn.setOnAction(event -> {
            try {
                formViewer.showForm("productModal", "Product");
                ProductModalController productModalController =
                        (ProductModalController) FormViewer.getLastLoadedController();
                productModalController.setViewFormType(ViewFormType.Order_Item);
                productModalController.setOrderItemController(this);
            } catch (Exception e) {
                log.error(e);
            }
        });

        saveBtn.setOnAction(event -> {
            try {
                OrderItem orderItem = OrderItem
                        .builder()
                        .order(Order.builder().id(Integer.parseInt(orderIdTxt.getText())).build())
                        .product(Product.builder().id(Integer.parseInt(productId.getText())).build())
                        .quantity(Integer.parseInt(quantity.getText()))
                        .build();

                orderItemValidator.validateOrderItem(orderItem);
                try {
                    orderItemService.save(orderItem);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "New OrderItem Saved", ButtonType.OK);
                    alert.show();
                    resetForm();
                    log.info("orderItem Saved : " + orderItem);
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Saving OrderItem Failed", ButtonType.OK);
                    alert.show();
                    log.error("orderItemService.save:" + e);
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

        refreshBtn.setOnAction(event -> resetForm());
    }

    public void loadData() throws Exception {
        try {
            Order order = orderService.findById(orderId);
            if (order != null) {
                orderIdTxt.setText(String.valueOf(orderId));
                resetForm();
            }
        } catch (Exception e) {
            log.error(e);
            throw new Exception(e);
        }
    }

    private void resetForm() {
        quantity.clear();
        product.clear();
        productId.clear();
        try {
            refreshTable(orderItemService.findAll(orderId));
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No Order Item Found", ButtonType.OK);
            alert.show();
            log.error(e);
        }
    }

    private void refreshTable(List<OrderItem> orderItemList) {
        ObservableList<OrderItem> orderItemObservableList = FXCollections.observableArrayList(orderItemList);
        orderItemTable.getItems().clear();

        productNameCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue()
                .getProduct().getName()));
        productPriceCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue()
                .getProduct().getPrice()).asString());
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        orderItemTable.setItems(orderItemObservableList);
    }

    public void fillProductField(Product selectedProduct) {
        Platform.runLater(() -> {
            if (product != null) {
                product.setText(selectedProduct.getName());
                productId.setText(String.valueOf(selectedProduct.getId()));
            }
        });
    }
}