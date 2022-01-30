/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

/**
 *
 * @author AnhTu
 */
public class User {
    String name;
    String pass;
    String salt;
    private String room;
    
        
    public User(String name, String pass, String salt) {
        this.name = name;
        this.pass = pass;
        this.salt = salt;
    }

    public User(String name) {
        this.name = name;
    }

    public String getRoom() {
        return room;
    }

    
    public void setRoom(String room) {
        this.room = room;
    }
    
    
}
