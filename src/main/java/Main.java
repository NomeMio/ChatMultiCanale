import org.mariadb.jdbc.Connection;
import utils.CostumLogger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.CallableStatement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        try{

            String connection_url = System.getenv("chatMulticanaleURL");
            String user = System.getenv("chatMulticanaleLogin");
            Connection connection = (Connection) DriverManager.getConnection(connection_url, user,"");
            CallableStatement test=connection.prepareCall("{call login(?,?,?,?)}");
            test.setString(1,"lav1");
            test.setString(2,"1");
            test.registerOutParameter(3, Types.NUMERIC);
            test.registerOutParameter(4,Types.VARCHAR);
            test.execute();
            CostumLogger.getInstance().logString(test.getString(4));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
