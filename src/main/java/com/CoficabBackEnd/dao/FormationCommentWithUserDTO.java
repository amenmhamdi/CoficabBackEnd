package com.CoficabBackEnd.dao;

import com.CoficabBackEnd.entity.FormationComment;
import com.CoficabBackEnd.entity.User;

public class FormationCommentWithUserDTO {
    private FormationComment formationComment;
    private User user;

    
    public FormationCommentWithUserDTO(FormationComment formationComment, User user) {
        this.formationComment = formationComment;
        this.user = user;
    }
    public FormationComment getFormationComment() {
        return formationComment;
    }
    public void setFormationComment(FormationComment formationComment) {
        this.formationComment = formationComment;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    
}
