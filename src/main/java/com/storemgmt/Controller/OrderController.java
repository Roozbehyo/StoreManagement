package com.storemgmt.Controller;

import com.storemgmt.Model.Entity.Customer;
import com.storemgmt.Model.Entity.Enum.FormState;
import com.storemgmt.Model.Entity.Enum.ViewFormType;
import com.storemgmt.Model.Entity.Order;
import com.storemgmt.Model.Entity.Seller;
import com.storemgmt.Model.Entity.StoreBranch;
import com.storemgmt.Model.Service.OrderService;
import com.storemgmt.Model.Validation.OrderValidator;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.extern.log4j.Log4j;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Log4j
public class OrderController implements Initializable {
    @FXML
    private TextField id, seller, customer, branch, customerId, sellerId, branchId;
    @FXML
    private TextField sellerNameSearchTxt, sellerFamilySearchTxt, customerNameSearchTxt, customerFamilySearchTxt;
    @FXML
    private DatePicker orderDate;
    @FXML
    private Button saveBtn, updateBtn, refreshBtn, addCustomerBtn, addSellerBtn, addBranchBtn, showOrderItemsBtn;
    @FXML
    private TableView<Order> orderTable;
    @FXML
    private TableColumn<Order, Integer> idCol;
    @FXML
    private TableColumn<Order, String> customerCol, sellerCol, orderDateCol;

    private final OrderValidator orderValidator = new OrderValidator();
    private final OrderService orderService = new OrderService();
    private final FormViewer formViewer = new FormViewer();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (FormViewer.orderFormState.equals(FormState.New)) {
            showOrderItemsBtn.setDisable(true);
            updateBtn.setDisable(true);
            refreshBtn.setVisible(false);
            log.info("Save Mode");
        }
        if (FormViewer.orderFormState.equals(FormState.Update)) {
            showOrderItemsBtn.setDisable(true);
            addCustomerBtn.setDisable(true);
            addBranchBtn.setDisable(true);
            orderDate.setDisable(true);
            saveBtn.setDisable(true);
            fillFieldsByMouseClick();
            log.info("Update Mode");
        }
        log.info("View initialized");
        resetForm();

        addCustomerBtn.setOnAction(event -> {
            try {
                formViewer.showForm("customerModal", "Customer");
                CustomerModalController customerModalController =
                        (CustomerModalController) FormViewer.getLastLoadedController();
                customerModalController.setViewFormType(ViewFormType.Order);
                customerModalController.setOrderController(this);
            } catch (Exception e) {
                log.error(e);
            }
        });

        addSellerBtn.setOnAction(event -> {
            try {
                formViewer.showForm("sellerModal", "Seller");
                SellerModalController sellerModalController =
                        (SellerModalController) FormViewer.getLastLoadedController();
                sellerModalController.setViewFormType(ViewFormType.Order);
                sellerModalController.setOrderController(this);
            } catch (Exception e) {
                log.error(e);
            }
        });

        addBranchBtn.setOnAction(event -> {
            try {
                formViewer.showForm("storeBranchModal", "Store Branch");
                BranchModalController branchModalController =
                        (BranchModalController) FormViewer.getLastLoadedController();
                branchModalController.setViewFormType(ViewFormType.Order);
                branchModalController.setOrderController(this);
            } catch (Exception e) {
                log.error(e);
            }
        });

        saveBtn.setOnAction(event -> {
            try {
                Order order = Order
                        .builder()
                        .customer(Customer.builder().id(Integer.parseInt(customerId.getText())).build())
                        .seller(Seller.builder().id(Integer.parseInt(sellerId.getText())).build())
                        .storeBranch(StoreBranch.builder().id(Integer.parseInt(branchId.getText())).build())
                        .orderDate(orderDate.getValue())
                        .build();

                orderValidator.validateOrder(order);
                try {
                    orderService.save(order);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "New Order Saved", ButtonType.OK);
                    alert.show();
                    resetForm();
                    log.info("order Saved : " + order);
                    formViewer.showForm("orderItem", "Order Detail");
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Saving Order Failed", ButtonType.OK);
                    alert.show();
                    log.error("orderService.save:" + e);
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
                Order order = Order
                        .builder()
                        .id(Integer.parseInt(id.getText()))
                        .seller(Seller.builder().id(Integer.parseInt(sellerId.getText())).build())
                        .storeBranch(StoreBranch.builder().id(Integer.parseInt(branchId.getText())).build())
                        .orderDate(orderDate.getValue())
                        .build();

                orderValidator.validateOrder(order);
                try {
                    orderService.edit(order);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Order Edited", ButtonType.OK);
                    alert.show();
                    resetForm();
                    log.info("Order Edited : " + order);
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Editing Process Failed");
                    alert.show();
                    log.error("Error in orderService.edit: ", e);
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

        showOrderItemsBtn.setOnAction(event -> {
            try {
                formViewer.showForm("orderItem", "Order Detail");
                OrderItemController orderItemController =
                        (OrderItemController) FormViewer.getLastLoadedController();
                Order order = Order.builder().id(Integer.parseInt(id.getText())).build();
                orderItemController.setOrderId(order.getId());
                orderItemController.loadData();
                log.info("Showing OrderItems");
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Something went wrong");
                alert.show();
                log.error("LoadData Error: ", e);
            }
        });

        refreshBtn.setOnAction(event -> {
            resetForm();
        });

        sellerNameSearchTxt.setOnKeyReleased(event -> {
            if (sellerNameSearchTxt.getLength() > 0) {
                try {
                    refreshTable(orderService.findAllBySellerFNameAndLName(sellerNameSearchTxt.getText(),
                            sellerFamilySearchTxt.getText()));
                } catch (Exception e) {
                    log.error(e);
                }
            }
        });
        sellerFamilySearchTxt.setOnKeyReleased(event -> {
            if (sellerFamilySearchTxt.getLength() > 0) {
                try {
                    refreshTable(orderService.findAllBySellerFNameAndLName(sellerNameSearchTxt.getText(),
                            sellerFamilySearchTxt.getText()));
                } catch (Exception e) {
                    log.error(e);
                }
            }
        });

        customerNameSearchTxt.setOnKeyReleased(event -> {
            if (customerNameSearchTxt.getLength() > 0) {
                try {
                    refreshTable(orderService.findAllByCustomerFNameAndLName(customerNameSearchTxt.getText(),
                            customerFamilySearchTxt.getText()));
                } catch (Exception e) {
                    log.error(e);
                }
            }
        });
        customerFamilySearchTxt.setOnKeyReleased(event -> {
            if (customerFamilySearchTxt.getLength() > 0) {
                try {
                    refreshTable(orderService.findAllByCustomerFNameAndLName(customerNameSearchTxt.getText(),
                            customerFamilySearchTxt.getText()));
                } catch (Exception e) {
                    log.error(e);
                }
            }
        });
    }

    private void resetForm() {
        id.clear();
        customer.clear();
        seller.clear();
        branch.clear();
        customerId.clear();
        sellerId.clear();
        branchId.clear();
        orderDate.setValue(null);
        sellerNameSearchTxt.clear();
        sellerFamilySearchTxt.clear();
        customerNameSearchTxt.clear();
        customerFamilySearchTxt.clear();
        showOrderItemsBtn.setDisable(true);
        try {
            refreshTable(orderService.findAll());
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No order Found", ButtonType.OK);
            alert.show();
            log.error(e);
        }
    }

    private void refreshTable(List<Order> orderList) {
        ObservableList<Order> orderObservableList = FXCollections.observableArrayList(orderList);
        orderTable.getItems().clear();

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue()
                .getCustomer().getLastname()));
        sellerCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue()
                .getSeller().getLastname()));
        orderDateCol.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        orderTable.setItems(orderObservableList);
    }

    private void fillFieldsByMouseClick() {
        orderTable.setOnMouseReleased(event -> {
            Order order = orderTable.getSelectionModel().getSelectedItem();
            if (order != null) {
                id.setText(String.valueOf(order.getId()));
                customer.setText(order.getCustomer().getFirstname()
                        .concat(" " + order.getCustomer().getLastname()));
                seller.setText(order.getSeller().getFirstname()
                        .concat(" " + order.getSeller().getLastname()));
                branch.setText(order.getStoreBranch().getBranchName());
                customerId.setText(String.valueOf(order.getCustomer().getId()));
                sellerId.setText(String.valueOf(order.getSeller().getId()));
                branchId.setText(String.valueOf(order.getStoreBranch().getId()));
                orderDate.setValue(order.getOrderDate());
                showOrderItemsBtn.setDisable(false);
            }
        });
    }

    public void fillCustomerField(Customer selectedCustomer) {
        Platform.runLater(() -> {
            if (customer != null) {
                customer.setText(selectedCustomer.getFirstname().concat(" " + selectedCustomer.getLastname()));
                customerId.setText(String.valueOf(selectedCustomer.getId()));
            }
        });
    }

    public void fillSellerField(Seller selectedSeller) {
        Platform.runLater(() -> {
            if (seller != null) {
                seller.setText(selectedSeller.getFirstname().concat(" " + selectedSeller.getLastname()));
                sellerId.setText(String.valueOf(selectedSeller.getId()));
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