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

    private static final Logger LOGGER = Logger.getLogger(SyncService.class);

    private String fromJira;
    private String toRepository;

    public SyncService sync(String fromJira) {
        this.fromJira = fromJira;
        return this;
    }

    public SyncService to(String repository) {
        this.toRepository = repository;
        return this;
    }

    public void execute(String jql) {

        JiraIssues jiraIssues = jiraRepository.getIssuesFromFilter();

        for (JiraIssues.Issue i : jiraIssues.issues) {
            try {
                boolean issueExists = gitHubRepository.issueExists(toRepository, i.fields.getCveId());
                if (!issueExists) {
                    String body = String.format("### Source: %s%s%n%s", fromJira, i.key, i.fields.description);
                    gitHubRepository.createIssue(toRepository, i.fields.getCleanSummary(), body, githubLabels);
                }
            } catch (UnsupportedEncodingException e) {
                LOGGER.errorf("Error: Failed to encode query", e);
            } catch (Exception e) {
                LOGGER.errorf("Error:", e);
            }
        }
    }
}
