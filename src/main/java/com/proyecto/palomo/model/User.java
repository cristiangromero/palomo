package com.proyecto.palomo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @Column(unique = true, nullable = false)
    private String userName;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = true)
    private String picture;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn (name = "userStatusId", nullable = true, updatable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Status userStatus;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "favorites",
            joinColumns = @JoinColumn(name = "userId", nullable = true),
            inverseJoinColumns = @JoinColumn(name="contact", nullable = true)
    )
    private List<User> contacts;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "userChats",
            joinColumns = @JoinColumn(name = "userId", nullable = true),
            inverseJoinColumns = @JoinColumn(name="chatId", nullable = true)
    )
    private List<Chat> chats;

    public void addContact(User contact){
        if(this.contacts == null){
            this.contacts = new ArrayList<>();
        }

        this.contacts.add(contact);
    }

    public void removeContact(User contact) {
        contacts.remove(contact);
    }
}
