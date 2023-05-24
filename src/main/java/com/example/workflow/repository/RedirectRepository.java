package com.example.workflow.repository;

import com.example.workflow.model.Redirect;
import com.example.workflow.model.RedirectId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RedirectRepository extends JpaRepository<Redirect, RedirectId> {


    boolean existsById(RedirectId redirectId);
}
