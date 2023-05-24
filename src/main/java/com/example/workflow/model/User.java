package com.example.workflow.model;



import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Entity
@Table(name = "users")
@Data
public class User {




    @Id
    @Column(name = "actor_username")
    private String username;

    @JsonIgnore
    @OneToOne
    @PrimaryKeyJoinColumn
    private Actor actor;

    private double availableBalance;
    private double pendingBalance;
}
