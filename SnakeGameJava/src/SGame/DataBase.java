package SGame;

import java.sql.*;

public class DataBase extends Configs {
    private static Connection dbConnection;
    // Соединение с СУБД MySQL.
    public static Connection getDbConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName + "?&useSSL=false&serverTimezone=UTC&verifyServerCertificate=false";
        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);
        return dbConnection;
    }

    // Метод, реализующий запрос и запись результата игры в базу данных.
    public static void writePlayersName(String playersName, String playersScore) throws SQLException, ClassNotFoundException {
        String insert = "INSERT INTO " + Const.PLAYER_TABLE + " (" + Const.PLAYERS_NAME + ", " + Const.PLAYERS_SCORE + ") " + " VALUES (?, ?)";
        PreparedStatement ps = getDbConnection().prepareStatement(insert);
        ps.setString(1, playersName);
        ps.setString(2, playersScore);
        ps.executeUpdate();
    }
}


