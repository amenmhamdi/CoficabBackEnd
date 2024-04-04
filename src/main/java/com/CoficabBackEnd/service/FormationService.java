package com.CoficabBackEnd.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.CoficabBackEnd.entity.Evaluation;
import com.CoficabBackEnd.entity.Formation;
import com.CoficabBackEnd.entity.FormationComment;
import com.CoficabBackEnd.entity.Notification;
import com.CoficabBackEnd.entity.User;
import com.CoficabBackEnd.repository.EvaluationRepository;
import com.CoficabBackEnd.repository.FormationCommentRepository;
import com.CoficabBackEnd.repository.FormationRepository;
import com.CoficabBackEnd.repository.UserRepository;

@Service
public class FormationService {

    private final FormationRepository formationRepository;
    private final UserRepository userRepository;
    private final EvaluationRepository evaluationRepository;
    private final FormationCommentRepository formationCommentRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    public FormationService(FormationRepository formationRepository, UserRepository userRepository,
            EvaluationRepository evaluationRepository, FormationCommentRepository formationCommentRepository) {
        this.formationRepository = formationRepository;
        this.userRepository = userRepository;
        this.evaluationRepository = evaluationRepository;
        this.formationCommentRepository = formationCommentRepository;
    }

    public Formation addFormation(Formation formation) {
        return this.formationRepository.save(formation);
    }

    public Formation updateFormation(Formation formation) {
        return this.formationRepository.save(formation);
    }

    public Set<Formation> getFormations() {
        return new HashSet<>(this.formationRepository.findAll());
    }

    public Formation getFormation(Long formationId) {
        return this.formationRepository.findById(formationId).orElse(null);
    }

    @Transactional
    public void deleteFormation(Long formationId) {
        Formation formation = formationRepository.findById(formationId)
                .orElseThrow(() -> new RuntimeException("Formation not found"));

        // Delete all associated comments
        List<FormationComment> comments = formationCommentRepository.findByFormationFid(formationId);
        formationCommentRepository.deleteAll(comments);

        // Remove the formation from all associated users
        for (User user : formation.getUsers()) {
            user.getFormations().remove(formation);
            userRepository.save(user); // Save the user to update changes
        }

        // Clear the list of users associated with the formation
        formation.getUsers().clear();

        // Delete all evaluations associated with the formation
        List<Evaluation> evaluations = evaluationRepository.findByFormationFid(formationId);
        evaluationRepository.deleteAll(evaluations);

        // Finally, delete the formation
        formationRepository.delete(formation);
    }

    public List<Formation> findAllByUserName(String userName) {
        return this.formationRepository.findAllByUsersUserName(userName);
    }

    @Transactional
    public Formation assignUsersToFormation(Long formationId, List<String> userNames, User sender) {
        Formation formation = formationRepository.findById(formationId)
                .orElseThrow(() -> new RuntimeException("Formation not found"));

        List<User> usersList = userRepository.findByUserNameIn(userNames);
        if (usersList.size() != userNames.size()) {
            throw new RuntimeException("One or more users not found");
        }

        for (User user : usersList) {
            user.getFormations().add(formation);
            // Notify the user about the assignment if needed
        }

        formation.getUsers().addAll(usersList);

        // Notify the users
        notifyUsersAssignedToFormation(usersList, formation, sender);

        // Save the updated entities
        userRepository.saveAll(usersList);
        return formationRepository.save(formation);
    }
    private void notifyUsersAssignedToFormation(List<User> users, Formation formation, User sender) {
        for (User user : users) {
            Notification notification = new Notification();
            // Construct the message with formatted title and sender username
            String message = "<b>" + formation.getTitle() + "</b>: You have been assigned to the formation by <b>" + sender.getUserName() + "</b>.";
            notification.setMessage(message);
            notification.setSender(sender); // Set sender
            notification.setReceiver(user);
            notificationService.addNotification(notification); // Save notification
        }
    }
    

    public Long getFormationCount() {
        return formationRepository.count();
    }

    // Get users associated with a formation
    public Set<User> getUsersForFormation(Long formationId) {
        Formation formation = formationRepository.findById(formationId)
                .orElseThrow(() -> new RuntimeException("Formation not found"));
        return formation.getUsers();
    }
}
