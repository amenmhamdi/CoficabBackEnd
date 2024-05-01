package com.CoficabBackEnd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.CoficabBackEnd.entity.FormationComment;

@Repository
public interface FormationCommentRepository extends JpaRepository<FormationComment, Long> {
    List<FormationComment> findByFormationFid(Long fid);
    List<FormationComment> findByUserUserName(String userName);
    List<FormationComment> findByFormationFidIn(List<Long> fids);
    
}
