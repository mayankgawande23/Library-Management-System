package com.library;
import com.library.config.DatabaseConfig;
import com.library.ui.ConsoleUI;
import org.mindrot.jbcrypt.BCrypt;

import javax.xml.crypto.Data;

public class Main {
    public static void main(String[] args) {
        ConsoleUI consoleUI = new ConsoleUI();
        DatabaseConfig db = new DatabaseConfig();
        db.setMainconnection();
//        String password = "123";
//
//        // Hash password
//        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
//
//        System.out.println(hashedPassword);
        consoleUI.start();


    }
}
