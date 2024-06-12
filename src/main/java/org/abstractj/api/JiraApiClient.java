package org.abstractj.api;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import org.abstractj.filter.AuthorizationHeaderFilter;
import org.abstractj.model.JiraIssues;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterProvider(AuthorizationHeaderFilter.class)
@RegisterRestClient(configKey = "jira-api")
public interface JiraApiClient {

    @GET
    @Path("/search")
    @Produces("application/json")
    JiraIssues searchIssues(@QueryParam("jql") String jql, @QueryParam("maxResults") int maxResults);
}
