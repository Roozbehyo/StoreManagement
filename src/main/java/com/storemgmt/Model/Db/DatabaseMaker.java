package com.storemgmt.Model.Db;

import com.storemgmt.Model.ConnectionProvider;

import java.io.File;
import java.util.Scanner;

public class DatabaseMaker {
    public static void createDatabase() throws Exception {
        File file = new File("./src/main/java/com/storemgmt/Model/Db/storeDb.sql");

        Scanner scanner = new Scanner(file);
        String lines = "";
        while(scanner.hasNextLine()) {
            lines += scanner.nextLine();
        }


        for (String sqlCommand : lines.split(";")) {
            ConnectionProvider.getConnectionProvider().getConnection().prepareStatement(sqlCommand).execute();
        }

    }
}
