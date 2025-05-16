package com.storemgmt.desktop.Controller;

import com.storemgmt.common.Entity.Enum.FormState;
import com.storemgmt.common.Entity.Seller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.extern.log4j.Log4j;

import java.io.IOException;

@Log4j
public class FormViewer {
    public static FormState sellerFormState, customerFormState, productFormState, orderFormState, inventoryFormState;
    public static FormState branchFormState;
    public static Seller loggedInSeller;
    @Getter
    private static Object lastLoadedController;

    public void showForm(String formName, String title) throws IOException {
        log.info(formName + "Form Starting");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/storemgmt/desktop/temps/" + formName + "View.fxml"));
        Pane root = loader.load();

        lastLoadedController = loader.getController();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
        log.info(formName + "Form Showed Up");
    }
}
