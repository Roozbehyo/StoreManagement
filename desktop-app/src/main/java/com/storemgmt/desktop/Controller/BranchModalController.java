package com.storemgmt.desktop.Controller;

import com.storemgmt.common.Entity.Enum.ViewFormType;
import com.storemgmt.common.Entity.StoreBranch;
import com.storemgmt.common.Service.StoreBranchService;
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
    @Setter
    private OrderController orderController;
    @Setter
    private InventoryController inventoryController;

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
                    orderController.fillBranchField(selectedBranch);
                }

                if (viewFormType == ViewFormType.Inventory){
                    inventoryController.fillBranchField(selectedBranch);
                }
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
