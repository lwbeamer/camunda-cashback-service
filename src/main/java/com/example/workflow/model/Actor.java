package com.example.workflow.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;


@Entity
@Table(name = "actors")
@Data
public class Actor {

    @Id
    private String username;


    @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;





}
