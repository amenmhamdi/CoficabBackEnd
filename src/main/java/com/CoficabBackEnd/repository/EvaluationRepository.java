package com.CoficabBackEnd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.CoficabBackEnd.entity.Evaluation;
@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {


    // Custom query method to find evaluations by formation ID
    List<Evaluation> findByFormationFid(Long formationId); // Adjusted method name
}
