package org.abstractj.api;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Encoded;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.abstractj.model.GitHubIssueRequest;
import org.abstractj.model.GitHubSearchResponse;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "github-api")
@RegisterProvider(org.abstractj.filter.AuthorizationHeaderFilter.class)
public interface GitHubApiClient {

    @GET
    @Path("/search/issues")
    @Produces("application/json")
    GitHubSearchResponse searchIssues(@Encoded @QueryParam("q") String query);

    @POST
    @Path("/repos/{owner}/{repo}/issues")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response createIssue(@PathParam("owner") String owner,
                         @PathParam("repo") String repo,
                         GitHubIssueRequest issueRequest);
}
