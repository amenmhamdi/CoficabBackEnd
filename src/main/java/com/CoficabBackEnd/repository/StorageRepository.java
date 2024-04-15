package com.CoficabBackEnd.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.CoficabBackEnd.entity.ImageData;

public interface StorageRepository extends JpaRepository<ImageData, Long> {
    Optional<ImageData> findByUserUserName(String userName);
}
