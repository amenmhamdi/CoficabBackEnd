package com.StoryCraftBackend.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class UserRoleId implements Serializable {

    @Column(name = "user_name")
    private String userName;

    @Column(name = "role_name")
    private String roleName;

    public UserRoleId() {
    }

    public UserRoleId(String userName, String roleName) {
        this.userName = userName;
        this.roleName = roleName;
    }

    // Getters and setters

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    // hashCode and equals

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRoleId that = (UserRoleId) o;
        return Objects.equals(userName, that.userName) &&
                Objects.equals(roleName, that.roleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, roleName);
    }
}
