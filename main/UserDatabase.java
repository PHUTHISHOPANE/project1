/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author AnhTu
 */
public class UserDatabase {

    private Connection conn;
    public final String DATABASE_NAME = "chat_db";
    public final String USERNAME = "root";
    public final String PASSWORD = "";
    public final String URL_MYSQL = "jdbc:mysql://localhost:3306/" + DATABASE_NAME;

    public final String USER_TABLE = "user_tb";

    private PreparedStatement pst;
    private ResultSet rs;
    private Statement st;

    public Connection connect() {
        try {
            //Class.forName("com.mysql.jdbc.Driver");     //Loading class `com.mysql.jdbc.Driver'. This is deprecated. The new driver class is `com.mysql.cj.jdbc.Driver'. The driver is automatically registered via the SPI and manual loading of the driver class is generally unnecessary.
            // Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL_MYSQL, USERNAME, PASSWORD);
            System.out.println("Connect successfull");
        } catch (SQLException ex) {
            Logger.getLogger(UserDatabase.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error connection!");
        }/* catch (ClassNotFoundException ex) {
            Logger.getLogger(UserDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        return conn;
    }

    public ResultSet getData() {
        try {
            st = conn.createStatement();
            rs = st.executeQuery("SELECT * FROM " + USER_TABLE);
        } catch (SQLException ex) {
            Logger.getLogger(UserDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }

        return rs;
    }

    public String getSalt(String user) {
        String salt = "";
        try {
            pst = conn.prepareStatement("SELECT salt from " + USER_TABLE + " WHERE name = '" + user + "'");
            rs = pst.executeQuery();
            while (rs.next()) {
                salt = rs.getString(1);
            }

        } catch (SQLException e) {
            Logger.getLogger(UserDatabase.class.getName()).log(Level.SEVERE, null, e);
        }
        return salt;
    }

    private void showData() {
        rs = getData();
        try {
            while (rs.next()) {
                System.out.printf("%-15s %-4s", rs.getString(1), rs.getString(2));
                System.out.println("");
            }

        } catch (SQLException ex) {
            Logger.getLogger(UserDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateUserPassword(String user, String pass, String salt) {
        try {
            pst = conn.prepareStatement("UPDATE user_tb SET pass = ?, salt = ? WHERE name = ?");
            pst.setString(1, pass);
            pst.setString(2, salt);
            pst.setString(3, user);
            pst.executeUpdate();
            
            System.out.println("Updated data successfully");
        } catch (SQLException ex) {
            Logger.getLogger(UserDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public int insertUser(User u) {
        //System.out.println("Before: name = "+u.name+" - pass = "+u.pass+" - salt = "+u.salt);
        try {
            pst = conn.prepareCall("INSERT INTO " + USER_TABLE + " VALUES ('" + u.name + "', '" + u.pass + "','" + u.salt + "')");
            int kq = pst.executeUpdate();
            if (kq > 0) {
                System.out.println("Insert successful!");
            }
            //System.out.println("After: name = "+u.name+" - pass = "+u.pass+" - salt = "+u.salt);
            return kq;
//        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
//            System.out.println("[L???I!] primary key '"+u.name+"' ???? t???n t???i, ko th???m th??m b???n ghi n??y!");
        } catch (SQLException ex) {
            Logger.getLogger(UserDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public int createUser(User u) {
        try {
            pst = conn.prepareStatement("INSERT INTO " + USER_TABLE + " VALUE(?,?);");
            pst.setString(1, u.name);
            pst.setString(2, u.pass);
            return pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UserDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public boolean checkUser(String name, String pass) {    //return 1 = account is correct
        try {
            pst = conn.prepareStatement("SELECT * FROM " + USER_TABLE + " WHERE name = '" + name + "' AND pass = '" + pass + "'");
            rs = pst.executeQuery();

            if (rs.first()) {
                //user and pass is correct:
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public void closeConnection() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (pst != null) {
                pst.close();
            }
            if (st != null) {
                st.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDatabase.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("[UserDatabase.java]close connection");
        }
    }

    public static void main(String[] args) {
        UserDatabase ud = new UserDatabase();
        ud.connect();
        ud.showData();
        ud.getSalt("Lepota");
        ud.closeConnection();
        System.out.println("============");
        ud.connect();
        //ud.insertUser(new User("huy", "3"));
        // ud.showData();
    }
}
