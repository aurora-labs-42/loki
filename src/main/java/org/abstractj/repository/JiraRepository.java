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