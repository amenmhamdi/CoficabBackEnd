package com.CoficabBackEnd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.CoficabBackEnd.entity.Formation;

@Repository
public interface FormationRepository extends JpaRepository<Formation, Long> {
    List<Formation> findAllByUsersUserName(String userName);
    List<Formation> findAllByFidIn(List<Long> fids);
}
