package com.storemgmt.Controller;

import com.storemgmt.Model.Entity.Enum.ViewFormType;
import com.storemgmt.Model.Entity.Seller;
import com.storemgmt.Model.Service.SellerService;
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
public class SellerModalController {

    @FXML
    private TableView<Seller> sellerTable;
    @FXML
    private TableColumn<Seller, String> fNameCol, lNameCol, usernameCol;
    @FXML
    private Button selectBtn, cancelBtn;

    @Setter
    private ViewFormType viewFormType;
    private Seller selectedseller;
    SellerService sellerService = new SellerService();

    @FXML
    public void initialize() {
        try {
            loadSellerData();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Nothing to show");
            alert.show();
            log.error(e);
        }

        selectBtn.setOnAction(e -> {
            selectedseller = sellerTable.getSelectionModel().getSelectedItem();
            if (selectedseller != null) {
                Stage stage = (Stage) selectBtn.getScene().getWindow();
                stage.close();
                if (viewFormType == ViewFormType.Order){
                    OrderController orderController = new OrderController();
                    orderController.fillSellerField(selectedseller);
                }
                if (viewFormType == ViewFormType.Store_Branch){
                    StoreBranchController storeBranchController = new StoreBranchController();
                    storeBranchController.fillSellerField(selectedseller);
                }
            }
        });
        cancelBtn.setOnAction(e -> {
            Stage stage = (Stage) selectBtn.getScene().getWindow();
            stage.close();
        });
    }

    public void loadSellerData() throws Exception {
        try {
            List<Seller> sellerList = sellerService.findAll();
            ObservableList<Seller> sellerObservableList = FXCollections.observableArrayList(sellerList);

            fNameCol.setCellValueFactory(new PropertyValueFactory<>("firstname"));
            lNameCol.setCellValueFactory(new PropertyValueFactory<>("lastname"));
            usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
            sellerTable.setItems(sellerObservableList);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
