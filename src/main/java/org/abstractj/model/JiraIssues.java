package org.abstractj.model;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JiraIssues {
    public List<Issue> issues;

    public static class Issue {
        public String key;
        public Fields fields;

        public static class Fields {
            public String summary;
            public String description;
            public String labels;

            public String getCveId() {
                final Pattern CVE_PATTERN = Pattern.compile("CVE-\\d{4}-\\d{4,7}");

                if (summary != null) {
                    Matcher matcher = CVE_PATTERN.matcher(summary);
                    if (matcher.find()) {
                        return matcher.group();
                    }
                }
                return getCleanSummary();
            }

            public String getCleanSummary() {
                int lastBracketIndex = summary.lastIndexOf('[');
                if (lastBracketIndex != -1) {
                    return summary.substring(0, lastBracketIndex).trim();
                }
                return summary;
            }
        }
    }
}
