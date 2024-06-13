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