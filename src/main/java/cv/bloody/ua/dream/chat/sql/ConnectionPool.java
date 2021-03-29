package cv.bloody.ua.dream.chat.sql;

import com.zaxxer.hikari.HikariDataSource;
import cv.bloody.ua.dream.chat.users.UserGroup;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionPool {

    private final HikariDataSource hikari;
    private Connection connection;
    private PreparedStatement selectByName;
    private PreparedStatement selectByAddres;
    private PreparedStatement insertUser;
    private PreparedStatement updateLastLogin;

    public ConnectionPool(String serverName , String port , String database , String user , String password) {
        hikari = new HikariDataSource();
        this.hikari.setMaximumPoolSize(1);
        this.hikari.setDataSourceClassName("com.mysql.cj.jdbc.MysqlDataSource");
        this.hikari.setPoolName("CoreHikariPool");
        this.hikari.addDataSourceProperty("serverName", serverName);
        this.hikari.addDataSourceProperty("port", port);
        this.hikari.addDataSourceProperty("databaseName", database);
        this.hikari.addDataSourceProperty("user", user);
        this.hikari.addDataSourceProperty("password", password);
        this.tryToCreateTable();
        try {
            createStatement();
        }catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalStateException();
        }
    }

    private void createStatement() throws SQLException{
        connection = hikari.getConnection();
        selectByName = connection.prepareStatement("SELECT * FROM `Users` WHERE `login` = (?)");
        selectByAddres = connection.prepareStatement("SELECT * FROM `Users` WHERE `addres` = (?)");
        insertUser = connection.prepareStatement("INSERT INTO `Users` (`login` , `password` , " +
                "`last_login` , `addres` , `display_name` , `user_group`) VALUES (" +
                "(?) , (?) , (?) , (?) , (?) , '" + UserGroup.USER.name() + "')");
        updateLastLogin = connection.prepareStatement("UPDATE `Users` SET `last_login` = (?) WHERE `login` = (?)");
    }

    public PreparedStatement getSelectByAddres() {
        return selectByAddres;
    }

    public PreparedStatement getInsertUser() {
        return insertUser;
    }

    public PreparedStatement getSelectByName() {
        return selectByName;
    }

    public PreparedStatement getUpdateLastLogin() {
        return updateLastLogin;
    }

    private void tryToCreateTable() {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = hikari.getConnection();
            statement = connection.createStatement();
            //Створювання таблиці користувачів
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `Users` (`login` varchar(20) , `password` TEXT NOT NULL ," +
                    " `last_login` LONG NOT NULL , `addres` varchar(20) , `display_name` varchar(32) ," +
                    "`user_group` varchar(64), PRIMARY KEY(`login`));");
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            if(statement != null) {
                try {
                    statement.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }


    public void close() {
        try {
            connection.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        try {
            selectByName.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        try {
            selectByAddres.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        try {
            insertUser.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        try {
            updateLastLogin.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        hikari.close();
    }

}
