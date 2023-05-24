package com.example.workflow.repository;

import com.example.workflow.model.Marketplace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MarketplaceRepository extends JpaRepository<Marketplace, Long> {

    @EntityGraph(attributePaths = {"rules"})
    @Override
    Optional<Marketplace> findById(Long aLong);


    @EntityGraph(attributePaths = {"rules"})
    @Override
    Page<Marketplace> findAll(Pageable pageable);
}
