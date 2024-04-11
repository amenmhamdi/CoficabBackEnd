package com.CoficabBackEnd.controller;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.CoficabBackEnd.entity.Formation;
import com.CoficabBackEnd.entity.User;
import com.CoficabBackEnd.service.FormationService;

@RestController
@RequestMapping("/formation")
public class FormationController {

    @Autowired
    private FormationService formationService;

    

    // Add formation
    @PostMapping("/addFormation")
    public ResponseEntity<Formation> addFormation(@RequestBody Formation formation) {
        Formation addedFormation = this.formationService.addFormation(formation);
        return ResponseEntity.ok(addedFormation);
    }

    // Get formation by ID
    @GetMapping("/getFormation/{formationId}")
    public Formation getFormation(@PathVariable("formationId") Long formationId) {
        return this.formationService.getFormation(formationId);
    }

    // Get all formations
    @GetMapping("/getAllFormations")
    public ResponseEntity<?> getAllFormations() {
        return ResponseEntity.ok(this.formationService.getFormations());
    }

    // Update formation
    @PutMapping("/updateFormation/{formationId}")
    public Formation updateFormation(@PathVariable("formationId") Long formationId, @RequestBody Formation formation) {
        formation.setFid(formationId);
        return this.formationService.updateFormation(formation);
    }

    // Delete formation
    @DeleteMapping("/deleteFormation/{formationId}")
    public void deleteFormation(@PathVariable("formationId") Long formationId) {
        this.formationService.deleteFormation(formationId);
    }

    // Get formations by user name
    @GetMapping("/userFormations/{userName}")
    public List<Formation> getUserFormations(@PathVariable("userName") String userName) {
        return this.formationService.findAllByUserName(userName);
    }

    // Assign users to a formation
    @PostMapping("/assignUsers/{formationId}")
    public ResponseEntity<Formation> assignUsersToFormation(@PathVariable Long formationId,
            @RequestBody List<Map<String, String>> users,
            Principal principal) {
        String senderUsername = principal.getName(); // Get the username of the logged-in user
        User sender = new User(); // Create a User object for the sender
        sender.setUserName(senderUsername);

        List<String> usernames = users.stream()
                .map(user -> user.get("value")) // Extract usernames from value property
                .collect(Collectors.toList());

        Formation updatedFormation = formationService.assignUsersToFormation(formationId, usernames, sender);
        return ResponseEntity.ok(updatedFormation);
    }

    // Get formation count
    @GetMapping("/getFormationCount")
    public ResponseEntity<Long> getFormationCount() {
        Long formationCount = this.formationService.getFormationCount();
        return ResponseEntity.ok(formationCount);
    }

    @GetMapping("/getFormationUsers/{formationId}")
    public ResponseEntity<Map<String, Set<User>>> getFormationUsers(@PathVariable("formationId") Long formationId) {
        Set<User> users = this.formationService.getUsersForFormation(formationId);
        Map<String, Set<User>> response = new HashMap<>();
        response.put("users", users);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/exportICalendar/{formationId}")
    public ResponseEntity<String> exportICalendar(@PathVariable Long formationId) {
        Formation formation = formationService.getFormation(formationId); // Retrieve the specific formation
        if (formation == null) {
            return ResponseEntity.notFound().build(); // Return 404 if formation not found
        }
        String iCalendarData = generateICalendarData(formation); // Generate iCalendar data for the formation
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setContentDispositionFormData("filename", "formation_" + formationId + ".ics"); // Set filename for
                                                                                                // download
        return new ResponseEntity<>(iCalendarData, headers, HttpStatus.OK);
    }

    // Method to generate iCalendar data for a specific formation
    // Method to generate iCalendar data for a specific formation
    private String generateICalendarData(Formation formation) {
        StringBuilder builder = new StringBuilder();
        builder.append("BEGIN:VCALENDAR\n");
        builder.append("VERSION:2.0\n");
        builder.append("BEGIN:VEVENT\n");
        builder.append("UID:").append(formation.getFid()).append("\n"); // Unique ID for the event
        builder.append("SUMMARY:").append(formation.getTitle()).append("\n"); // Title of the event
        builder.append("DESCRIPTION:")
                .append("ID: ").append(formation.getFid()).append("\\n")
                .append("Description: ").append(formation.getDescription()).append("\\n")
                .append("Company: ").append(formation.getCompany()).append("\\n")
                .append("Capacity: ").append(formation.getCapacity()).append("\\n")
                .append("Formation Type: ").append(formation.getFormationType()).append("\\n")
    
                // Include other fields as needed
                .append("\n");
        // Add other properties such as start date, end date, location, etc.
        // Format dates according to the iCalendar standard (e.g., YYYYMMDDTHHmmssZ)
        builder.append("DTSTART:").append(formatDateTime(formation.getStartDate(), formation.getStartTime()))
                .append("\n");
        builder.append("DTEND:").append(formatDateTime(formation.getEndDate(), formation.getEndTime())).append("\n");

        builder.append("LOCATION:").append(formation.getLocation()).append("\n");
        // Add other properties...
        builder.append("END:VEVENT\n");

        builder.append("END:VCALENDAR\n");
        return builder.toString();
    }

    // Method to format dates
    private String formatDateTime(String date, String time) {
        try {
            // Parse date string
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setTimeZone(TimeZone.getTimeZone("Africa/Lagos")); // Set timezone to West Central Africa
            Date parsedDate = dateFormat.parse(date);

            // Parse time string
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            timeFormat.setTimeZone(TimeZone.getTimeZone("Africa/Lagos")); // Set timezone to West Central Africa
            Date parsedTime = timeFormat.parse(time);

            // Combine date and time
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parsedDate);
            Calendar timeCalendar = Calendar.getInstance();
            timeCalendar.setTime(parsedTime);
            calendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE));
            calendar.set(Calendar.SECOND, 0); // Set seconds to zero

            // Format combined date and time
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
            outputFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Set output timezone to UTC
            return outputFormat.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return null; // Handle parsing error appropriately
        }
    }

}
