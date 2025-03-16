package com.storemgmt.Controller;

import com.storemgmt.Model.Entity.BranchSeller;
import com.storemgmt.Model.Entity.Enum.FormState;
import com.storemgmt.Model.Entity.Enum.ViewFormType;
import com.storemgmt.Model.Entity.Seller;
import com.storemgmt.Model.Entity.StoreBranch;
import com.storemgmt.Model.Service.BranchSellerService;
import com.storemgmt.Model.Service.StoreBranchService;
import com.storemgmt.Model.Validation.BranchSellerValidator;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Log4j
public class StoreBranchController implements Initializable {
    @FXML
    private Pane mainPane, branchSellerPane;
    @FXML
    private TextField branchId;
    @FXML
    private TextField branch;
    @FXML
    private TextField sellerTxt;
    @FXML
    private TextField sellerId;
    @FXML
    private TextField nameSearchTxt;
    @FXML
    private TextField hiddenBranchId;
    @FXML
    private Button showSellersBtn, saveBtn, updateBtn, deleteBtn, saveSellerBtn, updateSellerBtn, deleteSellerBtn,
            refreshBtn, addSellerBtn;
    @FXML
    private TableView<StoreBranch> storeBranchTable;
    @FXML
    private TableColumn<StoreBranch, Integer> branchIdCol;
    @FXML
    private TableColumn<StoreBranch, String> branchNameCol;
    @FXML
    private TableView<BranchSeller> branchSellerTable;
    @FXML
    private TableColumn<BranchSeller, String> sellerFNameCol, sellerLNameCol;

    private final BranchSellerValidator branchSellerValidator = new BranchSellerValidator();
    private final StoreBranchService storeBranchService = new StoreBranchService();
    private final BranchSellerService branchSellerService = new BranchSellerService();
    private final FormViewer formViewer = new FormViewer();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (FormViewer.branchFormState.equals(FormState.New)) {
            showSellersBtn.setDisable(true);
            updateBtn.setDisable(true);
            deleteBtn.setDisable(true);
            refreshBtn.setVisible(false);
            log.info("Save Mode");
        }
        if (FormViewer.branchFormState.equals(FormState.Update)) {
            showSellersBtn.setDisable(true);
            deleteSellerBtn.setDisable(true);
            saveBtn.setDisable(true);
            deleteBtn.setDisable(true);
            fillFieldsByMouseClick();
            log.info("Update Mode");
        }
        if (FormViewer.branchFormState.equals(FormState.Remove)) {
            showSellersBtn.setDisable(true);
            saveBtn.setDisable(true);
            updateBtn.setDisable(true);
            saveSellerBtn.setDisable(true);
            updateSellerBtn.setDisable(true);
            fillFieldsByMouseClick();
            log.info("Remove Mode");
        }
        log.info("View initialized");
        resetForm();

        saveBtn.setOnAction(event -> {
            try {
                StoreBranch storeBranch = StoreBranch
                        .builder()
                        .branchName(branch.getText())
                        .build();
                try {
                    storeBranchService.save(storeBranch);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "New Store Branch Saved", ButtonType.OK);
                    alert.show();
                    resetForm();
                    log.info("storeBranch Saved : " + storeBranch);
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Saving Store Branch Failed", ButtonType.OK);
                    alert.show();
                    log.error("storeBranchService.save:" + e);
                }
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please Enter Branch Name", ButtonType.OK);
                alert.show();
                log.error(e);
            }
        });
        updateBtn.setOnAction(event -> {
            try {
                StoreBranch storeBranch = StoreBranch
                        .builder()
                        .id(Integer.parseInt(branchId.getText()))
                        .branchName(branch.getText())
                        .build();
                try {
                    storeBranchService.edit(storeBranch);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION,
                            "Store Branch Edited", ButtonType.OK);
                    alert.show();
                    resetForm();
                    log.info("storeBranch Edited : " + storeBranch);
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR,
                            "Editing Process Failed", ButtonType.OK);
                    alert.show();
                    log.error("Error in storeBranchService.edit: ", e);
                }
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
                alert.show();
                log.error(e);
            }
        });
        deleteBtn.setOnAction(event -> {
            try {
                storeBranchService.remove(Integer.valueOf(branchId.getText()));
                Alert alert = new Alert(Alert.AlertType.INFORMATION,
                        "Store Branch Deleted", ButtonType.OK);
                alert.show();
                resetForm();
                log.info("storeBranch Deleted ,Id: " + branchId.getText());
                branchSellerService.remove(Integer.valueOf(branchId.getText()));
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Deleting Process Failed");
                alert.show();
                log.error("Error in storeBranchService.remove or branchSellerService.remove: ", e);
            }
        });

        showSellersBtn.setOnAction(event -> {
            try {
                hiddenBranchId.setText(branchId.getText());
                showAndLoadSellers();
                log.info("Showing BranchSeller Section");
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Something went wrong");
                alert.show();
                log.error(e);
            }
        });

        saveSellerBtn.setOnAction(event -> {
            try {
                BranchSeller branchSeller = BranchSeller
                        .builder()
                        .storeBranch(StoreBranch.builder().id(Integer.parseInt(hiddenBranchId.getText())).build())
                        .seller(Seller.builder().id(Integer.parseInt(sellerId.getText())).build())
                        .build();
                branchSellerValidator.validateBranchSeller(branchSeller);
                try {
                    branchSellerService.save(branchSeller);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION,
                            "New Seller Added To This Branch", ButtonType.OK);
                    alert.show();
                    resetBranchSellerForm();
                    log.info("seller Saved : " + branchSeller);
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR,
                            "Adding Seller Failed\n" + e.getMessage(), ButtonType.OK);
                    alert.show();
                    log.error("branchSellerService.save: " + e);
                }
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please Fill Seller Field", ButtonType.OK);
                alert.show();
                log.error(e);
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                alert.show();
                log.error(e);
            }
        });
        updateSellerBtn.setOnAction(event -> {
            try {
                BranchSeller branchSeller = BranchSeller
                        .builder()
                        .storeBranch(StoreBranch.builder().id(Integer.parseInt(hiddenBranchId.getText())).build())
                        .seller(Seller.builder().id(Integer.parseInt(sellerId.getText())).build())
                        .build();
                branchSellerValidator.validateBranchSeller(branchSeller);
                try {
                    branchSellerService.edit(branchSeller);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION,
                            "Seller Edited", ButtonType.OK);
                    alert.show();
                    resetBranchSellerForm();
                    log.info("storeBranch Edited : " + branchSeller);
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR,
                            "Editing Process Failed", ButtonType.OK);
                    alert.show();
                    log.error("Error in branchSellerService.edit: ", e);
                }
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please Fill Seller Field", ButtonType.OK);
                alert.show();
                log.error(e);
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
                alert.show();
                log.error(e);
            }
        });
        deleteSellerBtn.setOnAction(event -> {
            try {
                branchSellerService.remove(Integer.parseInt(hiddenBranchId.getText()),
                        Integer.parseInt(sellerId.getText()));
                Alert alert = new Alert(Alert.AlertType.INFORMATION,
                        "Seller Deleted", ButtonType.OK);
                alert.show();
                resetBranchSellerForm();
                log.info("Seller Deleted ,Id: " + sellerId.getText());
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Deleting Process Failed");
                alert.show();
                log.error("Error in branchSellerService.remove: ", e);
            }
        });

        addSellerBtn.setOnAction(event -> {
            try {
                formViewer.showForm("sellerModal", "Seller");
                SellerModalController sellerModalController =
                        (SellerModalController) FormViewer.getLastLoadedController();
                sellerModalController.setViewFormType(ViewFormType.Store_Branch);
                sellerModalController.setStoreBranchController(this);
            } catch (Exception e) {
                log.error(e);
            }
        });

        refreshBtn.setOnAction(event -> {
            branchSellerPane.setVisible(false);
            mainPane.setPrefHeight(338);
            mainPane.setLayoutX(0);
            mainPane.setLayoutY(0);
            mainPane.requestLayout();
            Stage stage = (Stage) mainPane.getScene().getWindow();
            stage.sizeToScene();
            resetForm();
        });

        nameSearchTxt.setOnKeyReleased(event -> {
            if (nameSearchTxt.getLength() > 0) {
                try {
                    refreshTable(storeBranchService.findByName(nameSearchTxt.getText()));
                } catch (Exception e) {
                    log.error(e);
                }
            }
        });
    }

    private void resetForm() {
        branchId.clear();
        branch.clear();
        showSellersBtn.setDisable(true);
        try {
            refreshTable(storeBranchService.findAll());
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No Store Branch Found", ButtonType.OK);
            alert.show();
            log.error(e);
        }
    }

    private void refreshTable(List<StoreBranch> storeBranchList) {
        ObservableList<StoreBranch> storeBranchObservableList = FXCollections.observableArrayList(storeBranchList);
        storeBranchTable.getItems().clear();

        branchIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        branchNameCol.setCellValueFactory(new PropertyValueFactory<>("branchName"));
        storeBranchTable.setItems(storeBranchObservableList);
    }

    private void fillFieldsByMouseClick() {
        storeBranchTable.setOnMouseReleased(event -> {
            StoreBranch storeBranch = storeBranchTable.getSelectionModel().getSelectedItem();
            if (storeBranch != null) {
                branchId.setText(String.valueOf(storeBranch.getId()));
                branch.setText(storeBranch.getBranchName());
                showSellersBtn.setDisable(false);
            }
        });
        branchSellerTable.setOnMouseReleased(event -> {
            BranchSeller branchSeller = branchSellerTable.getSelectionModel().getSelectedItem();
            if (branchSeller != null) {
                sellerId.setText(String.valueOf(branchSeller.getSeller().getId()));
                sellerTxt.setText(branchSeller.getSeller().getFirstname()
                        .concat(" " + branchSeller.getSeller().getLastname()));
            }
        });
    }

    public void fillSellerField(Seller selectedSeller) {
        Platform.runLater(() -> {
            if (sellerTxt != null) {
                sellerTxt.setText(selectedSeller.getFirstname().concat(" " + selectedSeller.getLastname()));
                sellerId.setText(String.valueOf(selectedSeller.getId()));
            }
        });
    }

    private void showAndLoadSellers() {
        branchSellerPane.setVisible(true);
        branchSellerPane.setLayoutX(-2);
        branchSellerPane.setLayoutY(335);
        mainPane.setPrefHeight(669);
        mainPane.setLayoutX(0);
        mainPane.setLayoutY(0);
        mainPane.requestLayout();
        Stage stage = (Stage) mainPane.getScene().getWindow();
        stage.sizeToScene();
        resetBranchSellerForm();
    }

    private void resetBranchSellerForm() {
        sellerId.clear();
        sellerTxt.clear();
        try {
            refreshBranchSellerTable(branchSellerService.findAllByStoreBranchId(Integer.valueOf((hiddenBranchId.getText()))));
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No Seller Found", ButtonType.OK);
            alert.show();
            log.error(e);
            branchSellerTable.getItems().clear();
        }
    }

    private void refreshBranchSellerTable(List<BranchSeller> branchSellerList) {
        ObservableList<BranchSeller> branchSellerObservableList = FXCollections.observableArrayList(branchSellerList);
        branchSellerTable.getItems().clear();

        sellerFNameCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue()
                .getSeller().getFirstname()));
        sellerLNameCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue()
                .getSeller().getLastname()));
        branchSellerTable.setItems(branchSellerObservableList);
    }
}