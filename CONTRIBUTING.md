# Contributing to AI Blast Radius Analyzer

Thank you for your interest in contributing to the **AI Blast Radius Analyzer** plugin! We aim to build the most sophisticated code-impact visualization engine within the JetBrains platform ecosystem.

## How to Contribute

1. **Fork the Repository** on GitHub.
2. **Clone your fork locally**:
   ```bash
   git clone https://github.com/YourUsername/ai-blast-radius-plugin.git
   ```
3. Set your internal IDE layout to natively import via the `build.gradle.kts` configuration framework.
4. **Branch Early:** Use proper descriptive branch tracking mapping to your intended feature.
   `git checkout -b feature/cool-new-idea`
5. **Enforce Enterprise Code Standards**:
   - Resolve all generated IDE Code Style Lints.
   - Adhere strictly to the defined JUnit testing threshold (Any modifications done to `BlastRadiusCalculator` need reciprocal unit testing parameters expanded inside `/src/test`).
6. **Submit a Draft PR**: Link to your target implementation. Let our maintainer team review your architecture bounds prior to merging.

## Running Tests

Before submitting any Pull Requests, ensure that locally bounded tests resolve globally via:
```bash
./gradlew clean test
```

We do not accept regressions. All pipelines must maintain a `0 error` compilation run environment.

## License

By contributing, you agree that your extensive adjustments and implementation iterations will be handled natively inside the project's default **MIT License**.
