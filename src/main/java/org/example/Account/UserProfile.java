package org.example.Account;

import jakarta.persistence.*;
import org.hibernate.annotations.NaturalId;


@Entity
@Table(name = "users")
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;
    @NaturalId
    private String login;
    private String password;
    private String email;
    public UserProfile(String login, String pass, String email){
        this.login = login;
        this.password = pass;
        this.email = email;
    }
    public UserProfile() {

    }
    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
