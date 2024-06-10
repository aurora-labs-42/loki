package org.abstractj.model;

public class GitHubIssueRequest {
    public String title;
    public String body;
    public String[] labels;

    public GitHubIssueRequest(String title, String body, String[] labels) {
        this.title = title;
        this.body = body;
        this.labels = labels;
    }
}