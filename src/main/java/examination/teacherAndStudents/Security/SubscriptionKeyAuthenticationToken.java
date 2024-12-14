package examination.teacherAndStudents.Security;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class SubscriptionKeyAuthenticationToken extends AbstractAuthenticationToken {

    private final String subscriptionKey;

    public SubscriptionKeyAuthenticationToken(String subscriptionKey) {
        super(null);
        this.subscriptionKey = subscriptionKey;
        setAuthenticated(false);
    }

    public SubscriptionKeyAuthenticationToken(String subscriptionKey, Object principal) {
        super(null);
        this.subscriptionKey = subscriptionKey;
        setAuthenticated(true); // This constructor should only be used when the user is authenticated
        setDetails(principal);
    }

    @Override
    public Object getCredentials() {
        return null; // Subscription key itself acts as the credential
    }

    @Override
    public Object getPrincipal() {
        return subscriptionKey;
    }
}
