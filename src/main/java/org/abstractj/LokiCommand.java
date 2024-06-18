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
package org.abstractj;

import io.quarkus.picocli.runtime.annotations.TopCommand;
import jakarta.inject.Inject;
import org.abstractj.service.SyncService;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@TopCommand
@Command(name = "loki", mixinStandardHelpOptions = true, description = "Sync Jiras with GitHub")
public class LokiCommand implements Runnable {

    @Inject
    SyncService syncService;

    @CommandLine.Option(names = {"--from-jira"}, description = "Source JIRA", required = true)
    String fromJira;

    @CommandLine.Option(names = {"--to-repo"}, description = "GitHub repository 'owner/repo'", required = true)
    String repository;

    @CommandLine.Option(names = {"--jql"}, description = "JQL query", required = true)
    String jql;

    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    @Override
    public void run() {
        if (fromJira == null || fromJira.isEmpty() || repository == null || repository.isEmpty() || jql == null || jql.isEmpty()) {
            CommandLine.usage(spec.commandLine(), System.err);
        } else {
            syncService.sync(fromJira).to(repository).execute(jql);
        }
    }
}
