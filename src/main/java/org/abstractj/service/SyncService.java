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
package org.abstractj.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.abstractj.model.JiraIssues;
import org.abstractj.repository.GitHubRepository;
import org.abstractj.repository.JiraRepository;
import org.jboss.logging.Logger;

import java.io.UnsupportedEncodingException;
import java.util.List;

@ApplicationScoped
public class SyncService {

    @Inject
    JiraRepository jiraRepository;

    @Inject
    GitHubRepository gitHubRepository;

    private static final Logger LOGGER = Logger.getLogger(SyncService.class);

    private String fromJira;
    private String toRepository;
    private List<String> labels;

    public SyncService sync(String fromJira) {
        this.fromJira = fromJira;
        return this;
    }

    public SyncService to(String repository) {
        this.toRepository = repository;
        return this;
    }

    public SyncService with(List<String> labels) {
        this.labels = labels;
        return this;
    }

    public void execute(String jql) {
        JiraIssues jiraIssues = jiraRepository.getIssuesFromFilter(fromJira, jql);
        int newIssuesCount = 0;

        if (jiraIssues != null) {
            for (JiraIssues.Issue i : jiraIssues.issues) {
                try {
                    boolean issueExists = gitHubRepository.issueExists(toRepository, i.fields.getCveId());
                    if (!issueExists) {
                        String body = String.format("### Source: %s%s%n%s", fromJira, i.key, i.fields.description);
                        gitHubRepository.createIssue(toRepository, i.fields.getCleanSummary(), body, labels);
                        newIssuesCount++;
                    }
                } catch (UnsupportedEncodingException e) {
                    LOGGER.errorf("Error: Failed to encode query", e);
                } catch (Exception e) {
                    LOGGER.errorf("Error:", e);
                }
            }
            LOGGER.infof("New issues created: %d", newIssuesCount);
        } else {
            LOGGER.infof("No issues found for query: %s", jql);
        }
    }
}
