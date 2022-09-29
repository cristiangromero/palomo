package com.proyecto.palomo.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;
    private String name;
    private String userName;
    private String email;
    private String password;
    private String picture;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "contacts")
    @JoinTable(
            name = "favorites",
            joinColumns = @JoinColumn(name = "userId", nullable = false),
            inverseJoinColumns = @JoinColumn(name="contact", nullable = false)
    )
    private List<User> contacts;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "userChats",
            joinColumns = @JoinColumn(name = "userId", nullable = false),
            inverseJoinColumns = @JoinColumn(name="chatId", nullable = false)
    )
    private List<Chat> chats;

    public void addContact(User contact){
        if(this.contacts == null){
            this.contacts = new ArrayList<>();
        }

        this.contacts.add(contact);
    }
}
