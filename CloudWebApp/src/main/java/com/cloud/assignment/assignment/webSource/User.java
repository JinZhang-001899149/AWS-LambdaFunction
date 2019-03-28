package com.cloud.assignment.assignment.webSource;

import com.cloud.assignment.assignment.Note.Note;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;


@Entity // This tells Hibernate to make a table out of this class
@Table(name = "user")

public class User {


    private String password;

    @Id
    private String email;

    //private String token;
    @OneToMany(mappedBy = "user")
    private List<Note> notes;


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /*public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;

    }*/
}

