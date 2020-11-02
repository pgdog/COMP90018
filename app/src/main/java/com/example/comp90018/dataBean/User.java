package com.example.comp90018.dataBean;

/**
 * The information of an user
 */
public class User {
    private String ID;
    private String image;
    private String userName;
    private String FirstName;
    private String MiddelName;
    private String lastName;

    //Could add more here

    public User(){}
    public User(String ID, String image,String userName, String firstName, String middelName, String lastName) {
        this.ID = ID;
        this.image=image;
        this.userName = userName;
        FirstName = firstName;
        MiddelName = middelName;
        this.lastName = lastName;
    }


    public String getID() {
        return ID;
    }

    public String getImage() {
        return image;
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

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public void setMiddelName(String middelName) {
        MiddelName = middelName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
