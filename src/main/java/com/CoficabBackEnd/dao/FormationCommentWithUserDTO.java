package com.CoficabBackEnd.dao;

import com.CoficabBackEnd.entity.Formation;
import com.CoficabBackEnd.entity.FormationComment;
import com.CoficabBackEnd.entity.User;

public class FormationCommentWithUserDTO {
    private FormationComment formationComment;
    private User user;
    private Formation formation;

    public FormationCommentWithUserDTO(FormationComment formationComment, User user, Formation formation) {
        this.formationComment = formationComment;
        this.user = user;
        this.formation = formation;
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

    public Formation getFormation() {
        return formation;
    }

    public void setFormation(Formation formation) {
        this.formation = formation;
    }

}
