package com.storemgmt.Controller;

import com.storemgmt.Model.Entity.Enum.FormState;
import com.storemgmt.Model.Entity.Customer;
import com.storemgmt.Model.Entity.Enum.Sex;
import com.storemgmt.Model.Service.CustomerService;
import com.storemgmt.Model.Validation.CustomerValidator;
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
public class CustomerController implements Initializable {
    @FXML
    private TextField id, firstName, lastName, nationalId, phone, nameSearchTxt, familySearchTxt;
    @FXML
    private DatePicker birthDate;
    @FXML
    private ComboBox<Sex> sexComboBox;
    @FXML
    private Button saveBtn, updateBtn, deleteBtn, refreshBtn;
    @FXML
    private TableView<Customer> customerTable;
    @FXML
    private TableColumn<Customer, Integer> idCol;
    @FXML
    private TableColumn<Customer, String> firstNameCol, lastNameCol, phoneCol;

    private final CustomerValidator customerValidator = new CustomerValidator();
    private final CustomerService customerService = new CustomerService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (FormViewer.customerFormState.equals(FormState.New)) {
            updateBtn.setDisable(true);
            deleteBtn.setDisable(true);
            refreshBtn.setVisible(false);
            log.info("Save Mode");
        }
        if (FormViewer.customerFormState.equals(FormState.Update)) {
            firstName.setDisable(true);
            lastName.setDisable(true);
            nationalId.setDisable(true);
            sexComboBox.setDisable(true);
            saveBtn.setDisable(true);
            deleteBtn.setDisable(true);
            fillFieldsByMouseClick();
            log.info("Update Mode");
        }
        if (FormViewer.customerFormState.equals(FormState.Remove)) {
            firstName.setDisable(true);
            lastName.setDisable(true);
            birthDate.setDisable(true);
            nationalId.setDisable(true);
            phone.setDisable(true);
            sexComboBox.setDisable(true);
            saveBtn.setDisable(true);
            updateBtn.setDisable(true);
            fillFieldsByMouseClick();
            log.info("Remove Mode");
        }
        log.info("View initialized");

        sexComboBox.setItems(FXCollections.observableArrayList(Sex.values()));
        sexComboBox.setPromptText("Select Sex");
        resetForm();

        saveBtn.setOnAction(event -> {
            try {
                Customer customer = Customer
                        .builder()
                        .firstname(firstName.getText())
                        .lastname(lastName.getText())
                        .birthdate(birthDate.getValue())
                        .nationalId(nationalId.getText())
                        .phoneNumber(phone.getText())
                        .sex(Sex.valueOf(sexComboBox.getSelectionModel().getSelectedItem().toString()))
                        .build();

                customerValidator.validateCustomer(customer);
                try {
                    customerService.save(customer);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "New Customer Saved", ButtonType.OK);
                    alert.show();
                    resetForm();
                    log.info("Customer Saved : " + customer);
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR,
                            "Saving Customer Failed\n" + e.getMessage(), ButtonType.OK);
                    alert.show();
                    log.error("customerService.save:" + e);
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
                Customer customer = Customer
                        .builder()
                        .id(Integer.parseInt(id.getText()))
                        .birthdate(birthDate.getValue())
                        .phoneNumber(phone.getText())
                        .build();

                customerValidator.validateCustomer(customer);
                try {
                    customerService.edit(customer);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Customer Edited", ButtonType.OK);
                    alert.show();
                    resetForm();
                    log.info("Customer Edited : " + customer);
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR,
                            "Editing Process Failed\n" + e.getMessage(), ButtonType.OK);
                    alert.show();
                    log.error("Error in customerService.edit: ", e);
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
                        customerService.remove(Integer.parseInt(id.getText()));
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Customer Deleted", ButtonType.OK);
                        alert.show();
                        resetForm();
                        log.info("Customer Deleted");
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
                    refreshTable(customerService.findAllByFNameAndLName(nameSearchTxt.getText(), familySearchTxt.getText()));
                } catch (Exception e) {
                    log.error(e);
                }
            }
        });
        familySearchTxt.setOnKeyReleased(event -> {
            if (familySearchTxt.getLength() > 0) {
                try {
                    refreshTable(customerService.findAllByFNameAndLName(nameSearchTxt.getText(), familySearchTxt.getText()));
                } catch (Exception e) {
                    log.error(e);
                }
            }
        });
    }

    private void resetForm() {
        id.clear();
        firstName.clear();
        lastName.clear();
        birthDate.setValue(null);
        nationalId.clear();
        phone.clear();
        sexComboBox.setValue(null);
        nameSearchTxt.clear();
        familySearchTxt.clear();
        try {
            refreshTable(customerService.findAll());
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No Customer Found", ButtonType.OK);
            alert.show();
            log.error(e);
        }
    }

    private void refreshTable(List<Customer> customerList) {
        ObservableList<Customer> customerObservableList = FXCollections.observableArrayList(customerList);
        customerTable.getItems().clear();

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        customerTable.setItems(customerObservableList);
    }

    private void fillFieldsByMouseClick() {
        customerTable.setOnMouseReleased(event -> {
            Customer customer = customerTable.getSelectionModel().getSelectedItem();
            if (customer != null) {
                id.setText(String.valueOf(customer.getId()));
                firstName.setText(customer.getFirstname());
                lastName.setText(customer.getLastname());
                birthDate.setValue(customer.getBirthdate());
                nationalId.setText(customer.getNationalId());
                phone.setText(customer.getPhoneNumber());
                sexComboBox.getSelectionModel().select(customer.getSex());
            }
        });
    }
}
