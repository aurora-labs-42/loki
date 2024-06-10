package org.abstractj.filter;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

import java.io.IOException;

@Provider
@Priority(Priorities.AUTHENTICATION)
@ApplicationScoped
public class AuthorizationHeaderFilter implements ClientRequestFilter {

    private static final Logger LOGGER = Logger.getLogger(AuthorizationHeaderFilter.class);

    private static final String JIRA_BEARER_TOKEN = System.getenv("JIRA_ACCESS_TOKEN");
    private static final String GITHUB_BEARER_TOKEN = System.getenv("GITHUB_ACCESS_TOKEN");

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        String targetHost = requestContext.getUri().getHost();

        if (targetHost.contains("issues.redhat.com")) {
            addAuthorizationHeader(requestContext, JIRA_BEARER_TOKEN, "Jira", "Bearer ");
        } else if (targetHost.contains("api.github.com")) {
            addAuthorizationHeader(requestContext, GITHUB_BEARER_TOKEN, "GitHub", "token ");
        }
    }

    private void addAuthorizationHeader(ClientRequestContext requestContext, String token, String serviceName, String tokenPrefix) {
        if (token != null && !token.isEmpty()) {
            String authHeader = tokenPrefix + token;
            requestContext.getHeaders().add("Authorization", authHeader);
            LOGGER.debugf("Added Authorization header for %s: %s", serviceName, authHeader);
        } else {
            LOGGER.warnf("Bearer token for %s is not set or empty", serviceName);
        }
    }
}
