package com.example.comp90018.dataBean;

/**
 * The information of an user
 */
public class User {
    private int ID;
    private String userName;
    private String FirstName;
    private String MiddelName;
    private String lastName;

    //Could add more here

    public User(int ID, String userName, String firstName, String middelName, String lastName) {
        this.ID = ID;
        this.userName = userName;
        FirstName = firstName;
        MiddelName = middelName;
        this.lastName = lastName;
    }


    public int getID() {
        return ID;
    }

    public String getUserName() {
        return userName;
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getMiddelName() {
        return MiddelName;
    }

    public String getLastName() {
        return lastName;
    }

}
