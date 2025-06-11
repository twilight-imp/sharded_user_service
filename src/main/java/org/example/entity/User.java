package org.example.entity;



import java.util.UUID;

public class User {
    private UUID id;
    private String name;

    private String email;

    protected User() {}

    public User(UUID id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }


    public UUID getId() {return id;}

    public void setId(UUID id) {this.id = id;}


    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    @Override
    public String toString() {return "User{id=" + id + ", name='" + name + "', email='" + email + "'}";}
}