package com.proyecto.palomo.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "userStatus")
@Data
public class UserStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userStatusId;
    private String name;
    @OneToMany(mappedBy = "userStatus",cascade = CascadeType.ALL)
    private List<User> users;
}
