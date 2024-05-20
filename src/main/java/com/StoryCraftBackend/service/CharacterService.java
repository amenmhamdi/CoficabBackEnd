package com.StoryCraftBackend.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.StoryCraftBackend.entity.Character;
import com.StoryCraftBackend.entity.Story;
import com.StoryCraftBackend.entity.Tag;
import com.StoryCraftBackend.repository.CharacterRepository;
import com.StoryCraftBackend.repository.StoryRepository;
import com.StoryCraftBackend.repository.TagRepository;

@Service
public class CharacterService {

    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private StoryRepository storyRepository;

    public Character createCharacter(Character character) {
        // Handle tags
        if (character.getTags() != null && !character.getTags().isEmpty()) {
            Set<Tag> tags = new HashSet<>();
            for (Tag tag : character.getTags()) {
                Tag existingTag = tagRepository.findByName(tag.getName());
                if (existingTag != null) {
                    tags.add(existingTag);
                } else {
                    tags.add(tagRepository.save(tag));
                }
            }
            character.setTags(tags);
        }

        // Associate character with story
        if (character.getStory() != null && character.getStory().getId() != null) {
            Story story = storyRepository.findById(character.getStory().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Story not found"));
            character.setStory(story);
        }

        return characterRepository.save(character);
    }

    public Character getCharacterById(Long id) {
        Optional<Character> character = characterRepository.findById(id);
        if (character.isPresent()) {
            return character.get();
        } else {
            throw new RuntimeException("Character not found for id :: " + id);
        }
    }

    public List<Character> getAllCharacters() {
        return characterRepository.findAll();
    }

    public Character updateCharacter(Long id, Character characterDetails) {
        Character character = getCharacterById(id);
        character.setName(characterDetails.getName());
        character.setRole(characterDetails.getRole());
        character.setDescription(characterDetails.getDescription());
        // Set other fields as needed
        return characterRepository.save(character);
    }

    public void deleteCharacter(Long id) {
        characterRepository.deleteById(id);
    }

    // Additional methods as needed
}
