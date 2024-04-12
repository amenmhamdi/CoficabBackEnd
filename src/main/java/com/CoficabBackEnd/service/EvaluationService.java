package com.CoficabBackEnd.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.CoficabBackEnd.entity.Evaluation;
import com.CoficabBackEnd.entity.Formation;
import com.CoficabBackEnd.entity.Notification;
import com.CoficabBackEnd.entity.User;
import com.CoficabBackEnd.repository.EvaluationRepository;
import com.CoficabBackEnd.repository.FormationRepository;

@Service
public class EvaluationService {

    private final EvaluationRepository evaluationRepository;
    private final FormationRepository formationRepository;

    @Autowired
    public EvaluationService(EvaluationRepository evaluationRepository, FormationRepository formationRepository) {
        this.evaluationRepository = evaluationRepository;
        this.formationRepository = formationRepository;
    }

    @Autowired
    private NotificationService notificationService;

    @Transactional
    public Evaluation addEvaluation(Evaluation evaluation, User sender) {
        // Check if the Formation object associated with the Evaluation is null
        if (evaluation.getFormation() == null) {
            throw new IllegalArgumentException("Formation is null in the Evaluation object.");
        }

        // Retrieve the Formation from the database based on its ID
        Formation formation = formationRepository.findById(evaluation.getFormation().getFid())
                .orElseThrow(() -> new RuntimeException("Formation not found"));

        // Set the retrieved Formation to the Evaluation
        evaluation.setFormation(formation);

        // Save the Evaluation
        Evaluation savedEvaluation = this.evaluationRepository.save(evaluation);

        // Update the associated Formation
        formation.getEvaluations().add(savedEvaluation);
        formationRepository.save(formation);

        // Notify users associated with the formation about the new evaluation
        notifyUsers(formation, savedEvaluation, sender, "CREATE");

        return savedEvaluation;
    }

    @Transactional
    public Evaluation updateEvaluation(Long evaluationId, Evaluation updatedEvaluation, User sender) {
        Evaluation existingEvaluation = this.evaluationRepository.findById(evaluationId)
                .orElseThrow(() -> new RuntimeException("Evaluation not found"));

        // Update the fields of the existing evaluation
        existingEvaluation.setFeedback(updatedEvaluation.getFeedback());
        existingEvaluation.setRating(updatedEvaluation.getRating());
        existingEvaluation.setSuggestions(updatedEvaluation.getSuggestions());

        // Calculate status based on the rating
        int rating = updatedEvaluation.getRating();
        String status;
        if (rating < 10) {
            status = "Unsatisfactory";
        } else if (rating >= 10 && rating < 15) {
            status = "Satisfactory";
        } else if (rating >= 15 && rating < 18) {
            status = "Good";
        } else {
            status = "Excellent";
        }
        
        existingEvaluation.setStatus(status);

        // Save the updated evaluation
        Evaluation savedEvaluation = this.evaluationRepository.save(existingEvaluation);

        // Notify users associated with the formation about the updated evaluation
        Formation formation = existingEvaluation.getFormation();
        notifyUsers(formation, savedEvaluation, sender, "UPDATE");

        return savedEvaluation;
    }
    
    public Evaluation getEvaluation(Long evaluationId) {
        return this.evaluationRepository.findById(evaluationId).orElse(null);
    }

    @Transactional
    public void deleteEvaluation(Long evaluationId, User sender) {
        // Retrieve the Evaluation from the database
        Evaluation evaluation = this.evaluationRepository.findById(evaluationId)
                .orElseThrow(() -> new RuntimeException("Evaluation not found"));

        // Remove the Evaluation from its associated Formation
        Formation formation = evaluation.getFormation();
        formation.getEvaluations().remove(evaluation);
        formationRepository.save(formation);

        // Delete the Evaluation
        this.evaluationRepository.deleteById(evaluationId);

        // Notify users associated with the formation about the deleted evaluation
        notifyUsers(formation, evaluation, sender, "DELETE");
    }

 

    // Method to get evaluations by formation ID
    public List<Evaluation> getEvaluationsByFormationId(Long formationId) {
        return evaluationRepository.findByFormationFid(formationId);
    }

    public List<Evaluation> getAllEvaluations() {
        return evaluationRepository.findAll();
    }

    private void notifyUsers(Formation formation, Evaluation evaluation, User sender, String actionType) {
        List<User> users = formation.getUsers().stream().distinct().collect(Collectors.toList());
        String senderUsername = (sender != null) ? sender.getUserName() : "System";

        for (User user : users) {
            Notification notification = new Notification();
            // Construct the message with formatted content based on the action type
            String message = "";
            switch (actionType) {
                case "CREATE":
                    message = "A new evaluation has been submitted for the formation <b>" + formation.getTitle()
                            + "</b> by <b>" + senderUsername + "</b>.";
                    break;
                case "UPDATE":
                    message = "An evaluation for the formation <b>" + formation.getTitle() + "</b> has been updated.";
                    break;
                case "DELETE":
                    message = "An evaluation for the formation <b>" + formation.getTitle() + "</b> has been deleted.";
                    break;
            }

            notification.setMessage(message);
            notification.setSender(sender);
            notification.setReceiver(user);
            notificationService.addNotification(notification);
        }
    }
}
