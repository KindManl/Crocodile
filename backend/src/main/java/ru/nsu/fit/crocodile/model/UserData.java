package ru.nsu.fit.crocodile.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "user_data")
public class UserData {
    @Id
    @GeneratedValue
    private Long id;

    private String username;

    private String password;

    @Column(unique = true)
    private String email;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    private Date registrationDate = new Date(System.currentTimeMillis());

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "statistics_id")
    private StatisticsData statistics = new StatisticsData();

    @ManyToMany
    @JoinTable(name = "user_data__friends",
            joinColumns =@JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id"
            ))
    private List<UserData> friends = new LinkedList<>();

    @ManyToMany(mappedBy = "outcomingFriendRequests")
    private List<UserData> incomingFriendRequests = new LinkedList<>();

    @ManyToMany
    private List<UserData> outcomingFriendRequests = new LinkedList<>();

    @ManyToMany
    @JoinTable(name = "user_data__roles",
            joinColumns =@JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"
            ))
    private List<Role> userRoles = new LinkedList<>();
}
