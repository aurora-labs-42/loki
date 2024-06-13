package org.abstractj.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.abstractj.model.JiraIssues;
import org.abstractj.repository.GitHubRepository;
import org.abstractj.repository.JiraRepository;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.io.UnsupportedEncodingException;

@ApplicationScoped
public class SyncService {

    @Inject
    JiraRepository jiraRepository;

    @Inject
    GitHubRepository gitHubRepository;

    @Inject
    @ConfigProperty(name = "loki.github.labels")
    String[] githubLabels;

    @Inject
    @ConfigProperty(name = "loki.jira.url")
    String jiraUrl;

    private static final Logger LOGGER = Logger.getLogger(SyncService.class);

    public void sync(String repository) {
        JiraIssues jiraIssues = jiraRepository.getIssuesFromFilter();

        for (JiraIssues.Issue i : jiraIssues.issues) {
            try {
                boolean issueExists = gitHubRepository.issueExists(repository, i.fields.getCveId());
                if (!issueExists) {
                    String body = String.format("### Source: %s%s%n%s", jiraUrl, i.key, i.fields.description);
                    gitHubRepository.createIssue(repository, i.fields.getCleanSummary(), body, githubLabels);
                }
            } catch (UnsupportedEncodingException e) {
                LOGGER.errorf("Error: Failed to encode query", e);
            } catch (Exception e) {
                LOGGER.errorf("Error:", e);
            }
        }
    }
}
