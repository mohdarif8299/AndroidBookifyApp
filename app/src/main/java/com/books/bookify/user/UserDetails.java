package com.books.bookify.user;
public class UserDetails {
    String name;
    String username;
    String password;
    String number;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public UserDetails() {
    }

    public UserDetails(String name, String username, String password, String number) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return  name+username +password;
    }
}
