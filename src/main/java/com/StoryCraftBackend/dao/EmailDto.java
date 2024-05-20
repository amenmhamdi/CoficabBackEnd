package com.StoryCraftBackend.dao;

public class EmailDto {
    private String to;
    private String subject;
    private String content;
    private String email; // Assurez-vous d'avoir ce champ

    // autres champs si nécessaire

    // Getters et setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    // Getters et setters
    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

