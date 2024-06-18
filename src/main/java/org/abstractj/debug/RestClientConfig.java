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
package org.abstractj.debug;

import jakarta.enterprise.inject.Produces;
import org.abstractj.api.JiraApiClient;
import org.eclipse.microprofile.rest.client.RestClientBuilder;

import java.net.URI;

// Uncomment the following line for debugging purposes
//@ApplicationScoped
public class RestClientConfig {

    @Produces
    public JiraApiClient createJiraClient() {
        return RestClientBuilder.newBuilder()
                .baseUri(URI.create("https://issues.redhat.com/rest/api/2"))
                .register(LoggingFilter.class)
                .build(JiraApiClient.class);
    }
}