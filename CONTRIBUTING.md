# Contributing

Use Java 11 and the included Gradle wrapper. Read the README for infrastructure setup and the current verification boundaries.

Before opening a pull request, run:

```bash
./gradlew check
```

The current example uses Spring Boot 2.5.7 and Java 11. Tests use JPA/H2 and do not establish Kafka Connect or database recovery behavior. Delivery changes should describe replay, ordering, and failure scenarios, with separate end-to-end evidence when available. Check startup scripts with `sh -n` after changing them.

Keep a pull request focused on one problem. Explain the behavior before and after, add a regression test for a behavior change, and state the checks actually run and their limits. Discuss substantial API or architecture changes in an issue first.

For a bug report, include the commit, Java and infrastructure versions, minimal reproduction, expected behavior, actual behavior, and relevant sanitized logs. Report suspected vulnerabilities through [SECURITY.md](SECURITY.md).

