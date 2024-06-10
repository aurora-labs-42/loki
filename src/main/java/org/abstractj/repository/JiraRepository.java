package org.abstractj.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.abstractj.api.JiraApiClient;
import org.abstractj.model.JiraIssues;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class JiraRepository {
    @Inject
    @RestClient
    JiraApiClient jiraApiClient;

    @Inject
    @ConfigProperty(name = "loki.jira.jql")
    String jql;

    public JiraIssues getIssuesFromFilter() {
        try {
            return jiraApiClient.searchIssues(jql, 50);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching issues from Jira", e);
        }
    }
}