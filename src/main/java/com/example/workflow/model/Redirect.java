package com.example.workflow.model;



import javax.persistence.*;
import lombok.Data;

import java.sql.Timestamp;


@Entity
@Table(name = "redirects")
@Data
public class Redirect {

    @EmbeddedId
    private RedirectId pk;


    private Timestamp time;


}
