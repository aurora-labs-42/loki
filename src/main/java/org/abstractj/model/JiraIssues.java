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
