package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

public class ConexionDB {
    private static final String CONFIG_FILE = "config.properties";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            Properties props = new Properties();
            FileInputStream input = new FileInputStream(CONFIG_FILE);
            props.load(input);

            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");

            conn = DriverManager.getConnection(url, user, password);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
