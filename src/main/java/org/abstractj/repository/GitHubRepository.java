package org.abstractj.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.abstractj.api.GitHubApiClient;
import org.abstractj.model.GitHubIssueRequest;
import org.abstractj.model.GitHubSearchResponse;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@ApplicationScoped
public class GitHubRepository {

    @Inject
    @RestClient
    GitHubApiClient client;

    public boolean issueExists(String repo, String cveId) throws UnsupportedEncodingException {
        String query = URLEncoder.encode("repo:" + repo + " " + cveId, StandardCharsets.UTF_8);
        GitHubSearchResponse response = client.searchIssues(query);

        // Check for bad credentials or rate limiting issues
        if (response.message != null) {
            if ("Bad credentials".equals(response.message)) {
                throw new IllegalStateException("Error: Bad credentials. Aborting script.");
            }
            if ("API rate limit exceeded".equals(response.message)) {
                throw new IllegalStateException("Error: API rate limit exceeded.");
            }
        }

        // Check if total_count is available
        if (response.total_count == null) {
            throw new IllegalStateException("Error: total_count not available in response.");
        }

        return response.total_count > 0;
    }

    public Response createIssue(String repository, String title, String body, String[] labels) {
        String[] repoParts = repository.split("/");
        String owner = repoParts[0];
        String repo = repoParts[1];

        GitHubIssueRequest issueRequest = new GitHubIssueRequest(title, body, labels);
        return client.createIssue(owner,repo,issueRequest);
    }
}

