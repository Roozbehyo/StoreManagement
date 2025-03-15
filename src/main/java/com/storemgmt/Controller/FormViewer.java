package com.storemgmt.Controller;

import com.storemgmt.Model.Entity.Enum.FormState;
import com.storemgmt.Model.Entity.Seller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j;

import java.io.IOException;

@Log4j
public class FormViewer {
    public static FormState sellerFormState, customerFormState, productFormState, orderFormState, inventoryFormState;
    public static FormState branchFormState;
    public static Seller loggedInSeller;

    public void showForm(String formName, String title) throws IOException {
        log.info(formName + "Form Starting");
        Stage stage = new Stage();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/temps/" + formName + "View.fxml")));
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
        log.info(formName + "Form Showed Up");
    }
}
