/*
 * Copyright (C) 2024 Bruno Oliveira<bruno@abstractj.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
@Priority(Priorities.AUTHORIZATION)
@ApplicationScoped
public class AuthorizationHeaderFilter implements ClientRequestFilter {

    private static final Logger LOGGER = Logger.getLogger(AuthorizationHeaderFilter.class);

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        String targetHost = requestContext.getUri().getHost();

        String jiraAccessToken = System.getenv("JIRA_ACCESS_TOKEN");
        String githubAccessToken = System.getenv("GITHUB_ACCESS_TOKEN");

        if (targetHost.contains("issues.redhat.com")) {
            addAuthorizationHeader(requestContext, jiraAccessToken, "Jira", "Bearer ");
        } else if (targetHost.contains("api.github.com")) {
            addAuthorizationHeader(requestContext, githubAccessToken, "GitHub", "token ");
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
