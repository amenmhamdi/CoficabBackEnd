package com.CoficabBackEnd.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.CoficabBackEnd.entity.FormationComment;
import com.CoficabBackEnd.repository.FormationCommentRepository;

@Service
public class FormationCommentService {

    private final FormationCommentRepository formationCommentRepository;

    @Autowired
    public FormationCommentService(FormationCommentRepository formationCommentRepository) {
        this.formationCommentRepository = formationCommentRepository;
    }

    public FormationComment addComment(FormationComment comment) {
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

    public List<FormationComment> findByFormationFid(Long fid) {
        return formationCommentRepository.findByFormationFid(fid);
    }

    public List<FormationComment> findByUserUserName(String userName) {
        return formationCommentRepository.findByUserUserName(userName);
    }

    public List<FormationComment> findByFormationFidIn(List<Long> fids) {
        return formationCommentRepository.findByFormationFidIn(fids);
    }

    public List<FormationComment> getAllComments() {
        return formationCommentRepository.findAll();
    }
}
