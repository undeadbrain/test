package Basket;

import java.lang.reflect.Field;
import java.util.ArrayList;

class User {
    private String name;
    private String ID;
    private String phone;

    public User(String name, String ID, String phone) {
        this.name = name;
        this.ID = ID;
        this.phone = phone;
    }

    public User(String... params) {
        this.name = params[0];
        this.ID = params[1];
        this.phone = params[2];
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Client name: " + name + "\n" +
                "Client ID: " + ID + "\n" +
                "Phone Client: " + phone + "\n";
    }
}