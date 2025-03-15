package com.storemgmt.Controller;

import com.storemgmt.Model.Entity.Enum.ViewFormType;
import com.storemgmt.Model.Entity.StoreBranch;
import com.storemgmt.Model.Service.StoreBranchService;
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
public class BranchModalController {

    @FXML
    private TableView<StoreBranch> branchTable;
    @FXML
    private TableColumn<StoreBranch, Integer> idCol;
    @FXML
    private TableColumn<StoreBranch, String> nameCol;
    @FXML
    private Button selectBtn, cancelBtn;

    @Setter
    private ViewFormType viewFormType;
    private StoreBranch selectedBranch;
    StoreBranchService storeBranchService = new StoreBranchService();

    @FXML
    public void initialize() {
        try {
            loadBranchData();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Nothing to show");
            alert.show();
            log.error(e);
        }

        selectBtn.setOnAction(e -> {
            selectedBranch = branchTable.getSelectionModel().getSelectedItem();
            if (selectedBranch != null) {
                Stage stage = (Stage) selectBtn.getScene().getWindow();
                stage.close();
                if (viewFormType == ViewFormType.Order){
                    OrderController orderController = new OrderController();
                    orderController.fillBranchField(selectedBranch);
                }

                if (viewFormType == ViewFormType.Inventory){
                    InventoryController inventoryController = new InventoryController();
                    inventoryController.fillBranchField(selectedBranch);
                }
//                TODO ADD View Form Type StoreBranch
//                if (viewFormType == ViewFormType.Store_Branch){}
            }
        });
        cancelBtn.setOnAction(e -> {
            Stage stage = (Stage) selectBtn.getScene().getWindow();
            stage.close();
        });
    }

    public void loadBranchData() throws Exception {
        try {
            List<StoreBranch> branchList = storeBranchService.findAll();
            ObservableList<StoreBranch> branchObservableList = FXCollections.observableArrayList(branchList);

            idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            nameCol.setCellValueFactory(new PropertyValueFactory<>("branchName"));
            branchTable.setItems(branchObservableList);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
