# loki

The `loki` project, embodies flexibility and adaptability. It seamlessly manages and synchronizes 
JIRA issues with GitHub repositories, making the management of security issues better, streamlining 
your workflow  and enhancing productivity.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:
```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:
```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/loki-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.

## Supported Commands

### `loki`

The `loki` command is used to sync JIRA issues with a GitHub repository.

#### Usage

```shell script
java -jar target/quarkus-app/quarkus-run.jar loki --from-jira --to-repo owner/repo
```

#### Options

- `--from-jira` : Specifies that the source is JIRA. This option is required.
- `--to-repo` : Specifies the target GitHub repository in the format `owner/repo`. This option is required.

## Environment Variables

To run the application, the following environment variables need to be set:

- `GITHUB_ACCESS_TOKEN` : GitHub token for authentication.
- `JIRA_ACCESS_TOKEN` : API token for JIRA authentication.
```
