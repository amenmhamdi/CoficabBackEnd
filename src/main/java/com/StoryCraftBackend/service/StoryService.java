package com.StoryCraftBackend.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.StoryCraftBackend.entity.Story;
import com.StoryCraftBackend.entity.Tag;
import com.StoryCraftBackend.repository.StoryRepository;
import com.StoryCraftBackend.repository.TagRepository;

@Service
public class StoryService {

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private TagRepository tagRepository;

    public Story createStory(Story story) {
        story.setCreatedDate(LocalDateTime.now());
        story.setUpdatedDate(LocalDateTime.now());

        // Handle tags
        if (story.getTags() != null && !story.getTags().isEmpty()) {
            Set<Tag> tags = new HashSet<>();
            for (Tag tag : story.getTags()) {
                Tag existingTag = tagRepository.findByName(tag.getName());
                if (existingTag != null) {
                    tags.add(existingTag);
                } else {
                    tags.add(tagRepository.save(tag));
                }
            }
            story.setTags(tags);
        }

        return storyRepository.save(story);
    }

    public Story getStoryById(Long id) {
        Optional<Story> story = storyRepository.findById(id);
        if (story.isPresent()) {
            return story.get();
        } else {
            throw new RuntimeException("Story not found for id :: " + id);
        }
    }

    public List<Story> getAllStories() {
        return storyRepository.findAll();
    }

    public Story updateStory(Long id, Story storyDetails) {
        Story story = getStoryById(id);
        story.setTitle(storyDetails.getTitle());
        story.setSummary(storyDetails.getSummary());
        story.setContent(storyDetails.getContent());
        story.setGenre(storyDetails.getGenre());
        story.setUpdatedDate(LocalDateTime.now());
        story.setStatus(storyDetails.getStatus());
        story.setViewCount(storyDetails.getViewCount());
        story.setLikeCount(storyDetails.getLikeCount());
        story.setShareCount(storyDetails.getShareCount());
        story.setCoverImage(storyDetails.getCoverImage());
        story.setPublishDate(storyDetails.getPublishDate());
        story.setFeatured(storyDetails.isFeatured());
        story.setAdultContent(storyDetails.isAdultContent());
        story.setLanguage(storyDetails.getLanguage());
        story.setWordCount(storyDetails.getWordCount());
        story.setAllowComments(storyDetails.isAllowComments());
        story.setRating(storyDetails.getRating());
        story.setRatingCount(storyDetails.getRatingCount());
        story.setLicense(storyDetails.getLicense());
        story.setFormat(storyDetails.getFormat());
        story.setLastEditedDate(LocalDateTime.now());
        story.setCommentCount(storyDetails.getCommentCount());
        story.setBookmarkCount(storyDetails.getBookmarkCount());
        story.setReportCount(storyDetails.getReportCount());
        story.setCompleted(storyDetails.isCompleted());
        story.setSerialized(storyDetails.isSerialized());
        story.setRelatedStories(storyDetails.getRelatedStories());
        story.setTags(storyDetails.getTags());
        story.setCharacters(storyDetails.getCharacters());
        return storyRepository.save(story);
    }

    public void deleteStory(Long id) {
        storyRepository.deleteById(id);
    }

    public List<Story> getFeaturedStories() {
        return storyRepository.findByIsFeatured(true);
    }

    public List<Story> getStoriesByGenre(String genre) {
        return storyRepository.findByGenre(genre);
    }

    public List<Story> getStoriesByUser(String userName) {
        return storyRepository.findByUser_UserName(userName);
    }

    // Additional methods as needed
}
