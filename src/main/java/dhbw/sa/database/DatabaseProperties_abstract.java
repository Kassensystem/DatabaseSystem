package dhbw.sa.database;

public abstract class DatabaseProperties_abstract {
    private String url = "jdbc:mysql://localhost:3306";
    private String database = "Kassensystem";
    private static String username;
    private static String password;

    DatabaseProperties_abstract(String username, String password) {
        this.username = username;
        this.password = password;
    }

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
