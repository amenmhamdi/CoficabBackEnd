package com.StoryCraftBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.StoryCraftBackend.entity.Character;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Long> {
    // Additional query methods if needed
}
