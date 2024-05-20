package com.StoryCraftBackend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.StoryCraftBackend.entity.Story;

@Repository
public interface StoryRepository extends JpaRepository<Story, Long> {
    List<Story> findByIsFeatured(boolean isFeatured);
    List<Story> findByGenre(String genre);
    List<Story> findByUser_UserName(String userName); // Adjusted to reference userName
    Optional<Story> findById(Long id);

}
