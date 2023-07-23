package gameboard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {
    private static Connection connection = null;
    
    private static String serverName = "sql.bsite.net\\MSSQL2016";
    private static String username = "lythainguyen_Scoreboard";
    private static String databaseName = "lythainguyen_Scoreboard";
    private static String password = "123";

    public DatabaseHandler() {
        connect();
    }
    
    private void connect(){
        String url = String.format(
                "jdbc:sqlserver://%s; databaseName = %s; integratedSecurity = false; username = %s; password = %s; trustServerCertificate=true;",
                serverName, databaseName, username, password);

        try {
            connection = DriverManager.getConnection(url);

        } catch (Exception e) {}
    }

    public void insert(String name, int score) {
        String sql = "insert into ranking values (?, ?)";

        try {
            PreparedStatement st = connection.prepareStatement(sql);

            st.setNString(1, name);
            st.setInt(2, score);

            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    
    public List<String[]> selectDescending() {
        List<String[]> list = new ArrayList<>();

        String sql = "select * from ranking order by score desc";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                list.add(new String[] {rs.getNString(1), rs.getString(2)});
            }
        } catch (SQLException e) {}

        return list;
    }
}
