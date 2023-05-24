package com.example.workflow.repository;


import com.example.workflow.model.Actor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActorRepository extends JpaRepository<Actor, String> {


}
