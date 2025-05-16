package com.storemgmt.desktop.Controller;

import com.storemgmt.common.Entity.Customer;
import com.storemgmt.common.Entity.Enum.ViewFormType;
import com.storemgmt.common.Service.CustomerService;
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
public class CustomerModalController {

    @FXML
    private TableView<Customer> customerTable;
    @FXML
    private TableColumn<Customer, String> fNameCol, lNameCol, phoneNumberCol;
    @FXML
    private Button selectBtn, cancelBtn;

    @Setter
    private ViewFormType viewFormType;
    private Customer selectedCustomer;
    CustomerService customerService = new CustomerService();
    @Setter
    private OrderController orderController;

    @FXML
    public void initialize() {
        try {
            loadCustomerData();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Nothing to show");
            alert.show();
            log.error(e);
        }

        selectBtn.setOnAction(e -> {
            selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
            if (selectedCustomer != null) {
                Stage stage = (Stage) selectBtn.getScene().getWindow();
                stage.close();
                if (viewFormType == ViewFormType.Order) {
                        orderController.fillCustomerField(selectedCustomer);
                }
            }
        });
        cancelBtn.setOnAction(e -> {
            Stage stage = (Stage) selectBtn.getScene().getWindow();
            stage.close();
        });
    }

    public void loadCustomerData() throws Exception {
        try {
            List<Customer> customerList = customerService.findAll();
            ObservableList<Customer> customerObservableList = FXCollections.observableArrayList(customerList);

            fNameCol.setCellValueFactory(new PropertyValueFactory<>("firstname"));
            lNameCol.setCellValueFactory(new PropertyValueFactory<>("lastname"));
            phoneNumberCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
            customerTable.setItems(customerObservableList);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
