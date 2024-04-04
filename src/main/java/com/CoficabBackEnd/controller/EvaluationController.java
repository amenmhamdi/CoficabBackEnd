package com.CoficabBackEnd.controller;

import java.security.Principal;
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

import com.CoficabBackEnd.entity.Evaluation;
import com.CoficabBackEnd.entity.User;
import com.CoficabBackEnd.repository.EvaluationRepository;
import com.CoficabBackEnd.service.EvaluationService;
import com.CoficabBackEnd.service.UserService;

@RestController
@RequestMapping("/evaluations")
public class EvaluationController {

    private final EvaluationService evaluationService;
    private final EvaluationRepository evaluationRepository; // Add EvaluationRepository
    private final UserService userService; // Autowire UserService

    @Autowired
    public EvaluationController(EvaluationService evaluationService, EvaluationRepository evaluationRepository, UserService userService) {
        this.evaluationService = evaluationService;
        this.evaluationRepository = evaluationRepository;
        this.userService = userService; // Assign the autowired UserService
    }

    @PostMapping("/addevaluation")
    public ResponseEntity<Evaluation> addEvaluation(@RequestBody Evaluation evaluation, Principal principal) {
        String senderUsername = principal.getName(); // Get the username of the logged-in user
        User sender = new User(); // Create a User object for the sender
        sender.setUserName(senderUsername);

        // Assuming evaluation object is received from the client
        Evaluation savedEvaluation = evaluationService.addEvaluation(evaluation, sender);
        return ResponseEntity.ok(savedEvaluation);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Evaluation> updateEvaluation(@PathVariable("id") Long id,
            @RequestBody Evaluation updatedEvaluation, Principal principal) {
        // Ensure the correct ID is set
        updatedEvaluation.setId(id);
        String senderUsername = principal.getName(); // Get the username of the logged-in user
        User sender = new User(); // Create a User object for the sender
        sender.setUserName(senderUsername);
    
        Evaluation result = evaluationService.updateEvaluation(id, updatedEvaluation, sender);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Evaluation> getEvaluation(@PathVariable("id") Long id) {
        Evaluation evaluation = evaluationService.getEvaluation(id);
        if (evaluation != null) {
            return new ResponseEntity<>(evaluation, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvaluation(@PathVariable("id") Long id, Principal principal) {
        String senderUsername = principal.getName(); // Get the username of the logged-in user
        User sender = new User(); // Create a User object for the sender
        sender.setUserName(senderUsername);
    
        evaluationService.deleteEvaluation(id, sender);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/user/{userName}") // Endpoint to get evaluations by username
    public List<Evaluation> getEvaluationsByUsername(@PathVariable String userName) {
        return evaluationRepository.findByUserUserName(userName);
    }

    @GetMapping("/formation/{formationId}") // Endpoint to get evaluations by formation ID
    public List<Evaluation> getEvaluationsByFormationId(@PathVariable Long formationId) {
        return evaluationRepository.findByFormationFid(formationId);
    }

    @GetMapping("/all") // Endpoint to get all evaluations
    public List<Evaluation> getAllEvaluations() {
        return evaluationService.getAllEvaluations();
    }
}
