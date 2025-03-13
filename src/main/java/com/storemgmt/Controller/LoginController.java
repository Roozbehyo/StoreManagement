package com.storemgmt.Controller;

import com.storemgmt.Model.Entity.Seller;
import com.storemgmt.Model.Service.SellerService;
import com.storemgmt.Model.Validation.SellerValidator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private TextField userTxt;
    @FXML
    private TextField passTxt;
    @FXML
    private Button loginBtn;

    private SellerService sellerService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loginBtn.setOnAction(event -> {
            try {
                FormViewer.loggedInSeller = sellerService.findByUsernameAndPassword(userTxt.getText(), passTxt.getText());
                FormViewer formViewer = new FormViewer();
                formViewer.showForm("main", "Home");

                loginBtn.getScene().getWindow().hide();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Login Error\n" + e.getMessage());
                alert.show();
            }
        });
    }
}
