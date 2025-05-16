package com.storemgmt.desktop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j;
import org.apache.log4j.BasicConfigurator;

@Log4j
public class MainApp extends Application {
        @Override
        public void start(Stage primaryStage) throws Exception {
            BasicConfigurator.configure();
            log.info("Starting Application");
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/com/storemgmt/desktop/temps/loginView.fxml")));
            primaryStage.setScene(scene);
            primaryStage.setTitle("Login");
            primaryStage.show();
        }
}
