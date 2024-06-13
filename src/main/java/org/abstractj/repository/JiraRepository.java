package org.abstractj.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import org.abstractj.api.JiraApiClient;
import org.abstractj.model.JiraIssues;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

@ApplicationScoped
public class JiraRepository {
    @Inject
    @RestClient
    JiraApiClient jiraApiClient;

    @Inject
    @ConfigProperty(name = "loki.jira.jql")
    String jql;

    private static final Logger LOGGER = Logger.getLogger(JiraRepository.class);

    public JiraIssues getIssuesFromFilter() {
        try {
            return jiraApiClient.searchIssues(jql, 50);
        } catch (WebApplicationException e) {
            LOGGER.errorf("Error fetching issues from Jira", e);
        } catch (Exception e) {
            LOGGER.errorf("Error", e);
        }
        return null;
    }
}