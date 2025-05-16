package com.storemgmt.desktop.Controller;

import com.storemgmt.common.Service.SellerService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import lombok.extern.log4j.Log4j;

import java.net.URL;
import java.util.ResourceBundle;

@Log4j
public class LoginController implements Initializable {
    @FXML
    private TextField userTxt;
    @FXML
    private TextField passTxt;
    @FXML
    private Button loginBtn;

    private final SellerService sellerService = new SellerService();

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
                log.error(e);
            }
        });
    }
}
