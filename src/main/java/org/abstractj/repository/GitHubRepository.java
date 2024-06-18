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
package org.abstractj.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.abstractj.api.GitHubApiClient;
import org.abstractj.model.GitHubIssueRequest;
import org.abstractj.model.GitHubSearchResponse;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@ApplicationScoped
public class GitHubRepository {

    @Inject
    @RestClient
    GitHubApiClient client;

    private static final Logger LOGGER = Logger.getLogger(GitHubRepository.class);

    // Check if an issue exists in a GitHub repository
    public boolean issueExists(String repo, String cveId) throws UnsupportedEncodingException {
        try {
            String query = URLEncoder.encode("repo:" + repo + " " + cveId, StandardCharsets.UTF_8);
            GitHubSearchResponse response = client.searchIssues(query);

            // Check if total_count is available
            if (response.total_count == null) {
                throw new IllegalStateException("Error: total_count not available in response.");
            }
            return response.total_count > 0;
        } catch (WebApplicationException e) {
            String responseBody = e.getResponse().readEntity(String.class);
            if (e.getResponse().getStatus() == Response.Status.UNAUTHORIZED.getStatusCode()) {
                LOGGER.errorf("Error: Bad credentials.", e);
            } else if (e.getResponse().getStatus() == Response.Status.FORBIDDEN.getStatusCode()) {
                LOGGER.errorf("Error: API rate limit exceeded.", e);
            }
            LOGGER.errorf("Error: %s", responseBody);
            throw e;
        }
    }

    // Create a new issue in a GitHub repository
    public Response createIssue(String repository, String title, String body, List<String> labels) {
        try {
            String[] repoParts = repository.split("/");
            String owner = repoParts[0];
            String repo = repoParts[1];

            GitHubIssueRequest issueRequest = new GitHubIssueRequest(title, body, labels);
            return client.createIssue(owner, repo, issueRequest);
        } catch (WebApplicationException e) {
            LOGGER.errorf("Error: Failed to create a new GitHub issue.", e);
            throw e;
        }
    }
}

