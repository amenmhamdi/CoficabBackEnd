package com.CoficabBackEnd.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.CoficabBackEnd.entity.FormationComment;
import com.CoficabBackEnd.service.FormationCommentService;

@RestController
@RequestMapping("/formationComment")
public class FormationCommentController {

    @Autowired
    private FormationCommentService formationCommentService;

    // Add formation comment
    @PostMapping("/addComment")
    public ResponseEntity<FormationComment> addComment(@RequestBody FormationComment comment) {
        FormationComment addedComment = this.formationCommentService.addComment(comment);
        return ResponseEntity.ok(addedComment);
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
    public List<FormationComment> getFormationComments(@PathVariable("formationId") Long formationId) {
        return this.formationCommentService.findByFormationFid(formationId);
    }

    // Get comments by user name
    @GetMapping("/userComments/{userName}")
    public List<FormationComment> getUserComments(@PathVariable("userName") String userName) {
        return this.formationCommentService.findByUserUserName(userName);
    }

    // Get all comments
    @GetMapping("/allComments")
    public List<FormationComment> getAllComments() {
        return this.formationCommentService.getAllComments();
    }

}
