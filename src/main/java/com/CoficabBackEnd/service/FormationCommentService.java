package com.CoficabBackEnd.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.CoficabBackEnd.dao.FormationCommentWithUserDTO;
import com.CoficabBackEnd.entity.Formation;
import com.CoficabBackEnd.entity.FormationComment;
import com.CoficabBackEnd.entity.User;
import com.CoficabBackEnd.repository.FormationCommentRepository;

@Service
public class FormationCommentService {

    private final FormationCommentRepository formationCommentRepository;

    @Autowired
    public FormationCommentService(FormationCommentRepository formationCommentRepository) {
        this.formationCommentRepository = formationCommentRepository;
    }

    public FormationComment addComment(FormationComment comment) {
        comment.setTimestamp(LocalDateTime.now());
        return formationCommentRepository.save(comment);
    }

    public FormationComment getComment(Long commentId) {
        return formationCommentRepository.findById(commentId).orElse(null);
    }

    public FormationComment updateComment(FormationComment comment) {
        Long commentId = comment.getId();
        FormationComment existingComment = formationCommentRepository.findById(commentId).orElse(null);
        if (existingComment != null) {
            // Update the attributes of the existing comment
            existingComment.setComment(comment.getComment());
            // Update other attributes as needed

            // Save the updated comment
            return formationCommentRepository.save(existingComment);
        } else {
            // Handle the case where the comment doesn't exist
            return null; // or throw an exception
        }
    }

    public void deleteComment(Long commentId) {
        formationCommentRepository.deleteById(commentId);
    }

    public List<FormationCommentWithUserDTO> findByFormationFidWithUser(Long fid) {
        List<FormationComment> comments = formationCommentRepository.findByFormationFid(fid);
        List<FormationCommentWithUserDTO> commentsWithUser = new ArrayList<>();
        for (FormationComment comment : comments) {
            User user = comment.getUser();
            FormationCommentWithUserDTO commentWithUser = new FormationCommentWithUserDTO(comment, user, null);
            commentsWithUser.add(commentWithUser);
        }

        return commentsWithUser;
    }

    public List<FormationComment> findByUserUserName(String userName) {
        return formationCommentRepository.findByUserUserName(userName);
    }

    public List<FormationComment> findByFormationFidIn(List<Long> fids) {
        return formationCommentRepository.findByFormationFidIn(fids);
    }

    public List<FormationCommentWithUserDTO> getAllComments() {
        List<FormationComment> comments = formationCommentRepository.findAll();
        List<FormationCommentWithUserDTO> commentsWithUsers = new ArrayList<>();
        for (FormationComment comment : comments) {
            User user = comment.getUser(); // Fetch the user associated with the comment
            Formation formation = comment.getFormation(); // Fetch the formation associated with the comment
            FormationCommentWithUserDTO commentWithUser = new FormationCommentWithUserDTO(comment, user, formation);
            commentsWithUsers.add(commentWithUser);
        }
        return commentsWithUsers;
    }

}
