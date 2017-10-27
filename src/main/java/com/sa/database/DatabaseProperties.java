package com.sa.database;

public class DatabaseProperties {
    private String url = "jdbc:mysql://localhost:3306";
    private String database = "Kassensystem";

    private String username = "DatabaseService";
    private String password = "password";


    public String getUrl() {
        return this.url;
    }
    public String getDatabase() { return this.database; }
    public String getUsername() {
        return this.username;
    }
    public String getPassword() {
        return this.password;
    }
}
