package com.storemgmt.desktop.Controller;

import com.storemgmt.common.Entity.Enum.FormState;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private MenuItem newSellerMnu, editSellerMnu;
    @FXML
    private MenuItem newCustomerMnu, editCustomerMnu, removeCustomerMnu;
    @FXML
    private MenuItem newProductMnu, editProductMnu, removeProductMnu;
    @FXML
    private MenuItem newOrderMnu, editOrderMnu;
    @FXML
    private MenuItem newInventoryMnu, editInventoryMnu, removeInventoryMnu;
    @FXML
    private MenuItem newBranchMnu, editBranchMnu, removeBranchMnu;
    @FXML
    private Label welcomeLabel;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        welcomeLabel.setText("Welcome Dear " + FormViewer.loggedInSeller.getFirstname()
                .concat(" " + FormViewer.loggedInSeller.getLastname()));
        newSellerMnu.setOnAction(e -> {
            FormViewer.sellerFormState = FormState.New;
            showSellerForm();
        });
        editSellerMnu.setOnAction(e -> {
            FormViewer.sellerFormState = FormState.Update;
            showSellerForm();
        });

        newCustomerMnu.setOnAction(e -> {
            FormViewer.customerFormState = FormState.New;
            showCustomerForm();
        });
        editCustomerMnu.setOnAction(e -> {
            FormViewer.customerFormState = FormState.Update;
            showCustomerForm();
        });
        removeCustomerMnu.setOnAction(e -> {
            FormViewer.customerFormState = FormState.Remove;
            showCustomerForm();
        });

        newProductMnu.setOnAction(e -> {
            FormViewer.productFormState = FormState.New;
            showProductForm();
        });
        editProductMnu.setOnAction(e -> {
            FormViewer.productFormState = FormState.Update;
            showProductForm();
        });
        removeProductMnu.setOnAction(e -> {
            FormViewer.productFormState = FormState.Remove;
            showProductForm();
        });

        newOrderMnu.setOnAction(e -> {
            FormViewer.orderFormState = FormState.New;
            showOrderForm();
        });
        editOrderMnu.setOnAction(e -> {
            FormViewer.orderFormState = FormState.Update;
            showOrderForm();
        });

        newInventoryMnu.setOnAction(e -> {
            FormViewer.inventoryFormState = FormState.New;
            showInventoryForm();
        });
        editInventoryMnu.setOnAction(e -> {
            FormViewer.inventoryFormState = FormState.Update;
            showInventoryForm();
        });
        removeInventoryMnu.setOnAction(e -> {
            FormViewer.inventoryFormState = FormState.Remove;
            showInventoryForm();
        });

        newBranchMnu.setOnAction(e -> {
            FormViewer.branchFormState = FormState.New;
            showBranchForm();
        });
        editBranchMnu.setOnAction(e -> {
            FormViewer.branchFormState = FormState.Update;
            showBranchForm();
        });
        removeBranchMnu.setOnAction(e -> {
            FormViewer.branchFormState = FormState.Remove;
            showBranchForm();
        });
    }

    public void showSellerForm() {
        try {
            FormViewer formViewer = new FormViewer();
            formViewer.showForm("seller", "Seller Information");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void showCustomerForm() {
        try {
            FormViewer formViewer = new FormViewer();
            formViewer.showForm("customer", "Customer Information");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void showProductForm() {
        try {
            FormViewer formViewer = new FormViewer();
            formViewer.showForm("product", "Product Information");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void showOrderForm() {
        try {
            FormViewer formViewer = new FormViewer();
            formViewer.showForm("order", "Order Information");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void showInventoryForm() {
        try {
            FormViewer formViewer = new FormViewer();
            formViewer.showForm("inventory", "Inventory Information");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void showBranchForm() {
        try {
            FormViewer formViewer = new FormViewer();
            formViewer.showForm("storeBranch", "Store Branch Information");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
