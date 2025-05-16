package com.storemgmt.desktop.Controller;

import com.storemgmt.common.Entity.Enum.FormState;
import com.storemgmt.common.Entity.Seller;
import com.storemgmt.common.Service.SellerService;
import com.storemgmt.common.Validation.SellerValidator;
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
public class SellerController implements Initializable {
    @FXML
    private TextField id, firstName, lastName, username, nameSearchTxt, familySearchTxt;

    @FXML
    private PasswordField password;

    @FXML
    private Button saveBtn, updateBtn, refreshBtn;

    @FXML
    private TableView<Seller> sellerTable;

    @FXML
    private TableColumn<Seller, Integer> idCol;

    @FXML
    private TableColumn<Seller, String> firstNameCol, lastNameCol, usernameCol;

    private final SellerValidator sellerValidator = new SellerValidator();
    private final SellerService sellerService = new SellerService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (FormViewer.sellerFormState.equals(FormState.New)) {
            updateBtn.setDisable(true);
            refreshBtn.setVisible(false);
            log.info("Save Mode");
        }
        if (FormViewer.sellerFormState.equals(FormState.Update)) {
            username.setDisable(true);
            saveBtn.setDisable(true);
            fillFieldsByMouseClick();
            log.info("Update Mode");
        }
        log.info("View initialized");
        resetForm();

        saveBtn.setOnAction(event -> {
            try {
                Seller seller = Seller
                        .builder()
                        .firstname(firstName.getText())
                        .lastname(lastName.getText())
                        .username(username.getText())
                        .password(password.getText())
                        .build();

                sellerValidator.validateSeller(seller);
                try {
                    sellerService.save(seller);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "New Seller Saved", ButtonType.OK);
                    alert.show();
                    resetForm();
                    log.info("Seller Saved : " + seller);
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR,
                            "Saving Seller Failed\n" + e.getMessage(), ButtonType.OK);
                    alert.show();
                    log.error("sellerService.save: " + e);
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
                Seller seller = Seller
                        .builder()
                        .id(Integer.parseInt(id.getText()))
                        .firstname(firstName.getText())
                        .lastname(lastName.getText())
                        .username(username.getText())
                        .password(password.getText())
                        .build();

                sellerValidator.validateSeller(seller);
                try {
                    sellerService.edit(seller);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Seller Edited", ButtonType.OK);
                    alert.show();
                    resetForm();
                    log.info("Seller Edited : " + seller);
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Editing Seller Failed", ButtonType.OK);
                    alert.show();
                    log.error("sellerService.edit: " + e);
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

        refreshBtn.setOnAction(event -> {
            resetForm();
        });

        nameSearchTxt.setOnKeyReleased(event -> {
            if (nameSearchTxt.getLength() > 0) {
                try {
                    refreshTable(sellerService.findAllByFNameAndLName(nameSearchTxt.getText(), familySearchTxt.getText()));
                } catch (Exception e) {
                    log.error(e);
                }
            }
        });
        familySearchTxt.setOnKeyReleased(event -> {
            if (familySearchTxt.getLength() > 0) {
                try {
                    refreshTable(sellerService.findAllByFNameAndLName(nameSearchTxt.getText(), familySearchTxt.getText()));
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
        username.clear();
        password.clear();
        nameSearchTxt.clear();
        familySearchTxt.clear();
        try {
            refreshTable(sellerService.findAll());
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No Seller Found", ButtonType.OK);
            alert.show();
            log.error(e);
        }
    }

    private void refreshTable(List<Seller> sellerList) {
        ObservableList<Seller> sellerObservableList = FXCollections.observableArrayList(sellerList);
        sellerTable.getItems().clear();

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        sellerTable.setItems(sellerObservableList);
    }

    private void fillFieldsByMouseClick() {
        sellerTable.setOnMouseReleased(event -> {
            Seller seller = sellerTable.getSelectionModel().getSelectedItem();
            if (seller != null) {
                id.setText(String.valueOf(seller.getId()));
                firstName.setText(seller.getFirstname());
                lastName.setText(seller.getLastname());
                username.setText(seller.getUsername());
            }
        });
    }
}
