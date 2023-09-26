package DTOs;

public class AccountLoginAndDetailsDTO {
    String loginHashed;
    String firstName;
    String lastName;

    public AccountLoginAndDetailsDTO(String loginHashed, String firstName, String lastName) {
        this.loginHashed = loginHashed;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getLoginHashed() {
        return loginHashed;
    }

    public void setLoginHashed(String loginHashed) {
        this.loginHashed = loginHashed;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
