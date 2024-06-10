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
    boolean fromJira;

    @CommandLine.Option(names = {"--to-repo"}, description = "GitHub repository 'owner/repo'", required = true)
    String repository;

    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    @Override
    public void run() {
        if (fromJira) {
            syncService.sync(repository);
        } else {
            CommandLine.usage(spec.commandLine(), System.err);
        }
    }
}
