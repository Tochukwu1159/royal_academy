package examination.teacherAndStudents.Security;

import examination.teacherAndStudents.service.SchoolService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SubscriptionKeyAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    @Autowired
    private SchoolService schoolService;

    protected SubscriptionKeyAuthenticationFilter() {
        super(new AntPathRequestMatcher("/api/**")); // Endpoint pattern to be intercepted
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        // Extract subscription key from request header or query parameter
        String subscriptionKey = request.getHeader("Subscription-Key");
        if (subscriptionKey == null || subscriptionKey.isEmpty()) {
            subscriptionKey = request.getParameter("subscriptionKey");
        }

        // Validate subscription key and check if subscription has expired
        if (subscriptionKey == null || subscriptionKey.isEmpty() || !schoolService.isValidSubscriptionKey(subscriptionKey)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Invalid or expired subscription key");
            response.getWriter().flush();
            response.getWriter().close();
            return null;
        }

        // Authentication successful
        return getAuthenticationManager().authenticate(new SubscriptionKeyAuthenticationToken(subscriptionKey));
    }
}

