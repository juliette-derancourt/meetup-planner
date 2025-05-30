### Generate HTML test report

```shell
  jbang org.opentest4j.reporting:open-test-reporting-cli:0.2.3:standalone \
      html-report \
          target/junit-reports/open-test-report.xml \
          --output target/junit-reports/open-test-report.html \
          --open
```