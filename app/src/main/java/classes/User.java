package classes;

public class User {
    private String userID;
    private String name ;
    private String phNumber;
    private String email;
    private String role ;


    public User() {
    }
    public User(String userID, String name, String phNumber, String email , String role) {
        this.userID = userID;
        this.name = name;
        this.phNumber = phNumber;
        this.email = email;
        this.role = role ;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserID() {
        return userID;
    }
    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhNumber() {
        return phNumber;
    }

    public void setPhNumber(String phNumber) {
        this.phNumber = phNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
