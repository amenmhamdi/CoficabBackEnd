package com.StoryCraftBackend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.StoryCraftBackend.entity.Story;
import com.StoryCraftBackend.repository.TagRepository;
import com.StoryCraftBackend.service.StoryService;

@RestController
@RequestMapping("/stories")
public class StoryController {

    @Autowired
    private StoryService storyService;

    @Autowired
    private TagRepository tagRepository;

    @PostMapping
    public ResponseEntity<Story> createStory(@RequestBody Story story) {
        Story createdStory = storyService.createStory(story);
        return new ResponseEntity<>(createdStory, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Story> getStoryById(@PathVariable Long id) {
        Story story = storyService.getStoryById(id);
        return ResponseEntity.ok(story);
    }

    @GetMapping
    public ResponseEntity<List<Story>> getAllStories() {
        List<Story> stories = storyService.getAllStories();
        return ResponseEntity.ok(stories);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Story> updateStory(@PathVariable Long id, @RequestBody Story storyDetails) {
        Story updatedStory = storyService.updateStory(id, storyDetails);
        return ResponseEntity.ok(updatedStory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStory(@PathVariable Long id) {
        storyService.deleteStory(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/featured")
    public ResponseEntity<List<Story>> getFeaturedStories() {
        List<Story> stories = storyService.getFeaturedStories();
        return ResponseEntity.ok(stories);
    }

    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<Story>> getStoriesByGenre(@PathVariable String genre) {
        List<Story> stories = storyService.getStoriesByGenre(genre);
        return ResponseEntity.ok(stories);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Story>> getStoriesByUser(@PathVariable String userName) {
        List<Story> stories = storyService.getStoriesByUser(userName);
        return ResponseEntity.ok(stories);
    }
}
