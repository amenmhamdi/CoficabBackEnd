package com.CoficabBackEnd.controller;

import java.time.LocalDateTime;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.CoficabBackEnd.dao.FormationCommentWithUserDTO;
import com.CoficabBackEnd.entity.Formation;
import com.CoficabBackEnd.entity.FormationComment;
import com.CoficabBackEnd.entity.User;
import com.CoficabBackEnd.repository.FormationRepository;
import com.CoficabBackEnd.repository.UserRepository;
import com.CoficabBackEnd.service.FormationCommentService;

@RestController
@RequestMapping("/formationComment")
public class FormationCommentController {

    @Autowired
    private FormationCommentService formationCommentService;
    @Autowired
    private FormationRepository formationRepository;
    @Autowired
    private UserRepository userRepository;

    // Add formation comment
    @PostMapping("/addComment")
    public ResponseEntity<FormationComment> addComment(@RequestParam Long formationId, @RequestParam String username,
            @RequestBody FormationComment comment) {
        try {
            // Retrieve Formation object using FormationRepository
            Formation formation = formationRepository.findById(formationId).orElse(null);
    
            // Retrieve User object using UserRepository
            User user = userRepository.findByUserName(username);
    
            // Check if either Formation or User object is null
            if (formation == null || user == null) {
                // Handle the case where either Formation or User object is null
                return ResponseEntity.badRequest().build(); // Return a bad request response
            }
    
            // Set Formation and User objects in the comment
            comment.setFormation(formation);
            comment.setUser(user);
            
            // Check if timestamp is provided in the request
            if (comment.getTimestamp() == null) {
                // If not provided, set it to the current time
                comment.setTimestamp(LocalDateTime.now());
            }
    
            // Save the comment with updated fields
            FormationComment addedComment = this.formationCommentService.addComment(comment);
    
            return ResponseEntity.ok(addedComment);
        } catch (Exception e) {
            // Log the exception for debugging purposes
            e.printStackTrace();
            // Return an internal server error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    

    // Get formation comment by ID
    @GetMapping("/getComment/{commentId}")
    public FormationComment getComment(@PathVariable("commentId") Long commentId) {
        return this.formationCommentService.getComment(commentId);
    }

    // Update formation comment
    @PutMapping("/updateComment/{commentId}")
    public FormationComment updateComment(@PathVariable("commentId") Long commentId,
            @RequestBody FormationComment comment) {
        comment.setId(commentId);
        return this.formationCommentService.updateComment(comment);
    }

    // Delete formation comment
    @DeleteMapping("/deleteComment/{commentId}")
    public void deleteComment(@PathVariable("commentId") Long commentId) {
        this.formationCommentService.deleteComment(commentId);
    }

    // Get comments by formation ID
    @GetMapping("/formationComments/{formationId}")
    public List<FormationCommentWithUserDTO> getFormationCommentsWithUser(
            @PathVariable("formationId") Long formationId) {
        return this.formationCommentService.findByFormationFidWithUser(formationId);
    }

    // Get comments by user name
    @GetMapping("/userComments/{userName}")
    public List<FormationComment> getUserComments(@PathVariable("userName") String userName) {
        return this.formationCommentService.findByUserUserName(userName);
    }

    // Get all comments
    @GetMapping("/allComments")
    public List<FormationCommentWithUserDTO> getAllComments() {
        return this.formationCommentService.getAllComments();
    }
    

}
