package teamwordk.WebServiceApi.login.model;

/**
 * En klass för att hantera registreringsförfrågningar.
 * Den innehåller de uppgifter som användaren måste skicka för att registrera sig.
 */
public class RegistrationRequest {

    private String username;
    private String email;
    private String password;

    public RegistrationRequest() {
    }

    public RegistrationRequest(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
