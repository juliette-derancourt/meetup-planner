## Meetup planner

The goal of this small application is to showcase usage of several testing frameworks
and libraries (such as [JUnit 5](https://junit.org/junit5/),
[AssertJ](https://assertj.github.io/doc/),
[Spring](https://docs.spring.io/spring-framework/reference/testing.html),
[ApprovalTest](https://github.com/approvals/approvaltests.java)).

### Generate HTML test report

After a test execution, run the following command
to [generate an HTML report](https://github.com/ota4j-team/open-test-reporting#html-report):

```shell
  jbang org.opentest4j.reporting:open-test-reporting-cli:0.2.3:standalone \
      html-report \
          target/junit-reports/open-test-report.xml \
          --output target/junit-reports/open-test-report.html \
          --open
```