package DTOs;

import Models.Employees.Role;

public class PasswordRoleDTO {
    String password;
    Role role;

    public PasswordRoleDTO(String password, Role role) {
        this.password = password;
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

}
