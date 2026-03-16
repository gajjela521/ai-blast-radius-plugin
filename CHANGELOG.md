# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.0-SNAPSHOT] - 2026-03-16

### Added
- **Core Blast Radius Calculator API**: Evaluates structural impact boundaries utilizing IntelliJ platform `search.ReferencesSearch`.
- **Git Coupled Change History**: Implemented 6-month timeline evaluations parsing `GitHistoryUtils` to aggregate co-changing files.
- **Enterprise-ready Severity Rules Tracker**: Added logical tiering metrics generating impact totals ranging sequentially from `LOW` to `CRITICAL` risk categories.
- **Visual Gutter Hooks**: Real-time evaluation indicators rendered via `BlastRadiusLineMarkerProvider` + `ColorsIcon`.
- **Intelligent Context Actions**: Triggerable manual `Analyze Blast Radius` context command available strictly scoped to Java contextual blocks.
- **Robust Enterprise test layer**: Fully compatible headless Gradle `test` tasks via JUnit 5.
- Jetbrains plugin generation workflow compliant with IDEA `232.*` through `242.*` APIs.

### Fixed
- Stabilized and integrated the `getChanges()` array fetch payload handling for the IntelliJ `GitCommit` abstract schema.

### Changed
- Re-architected Java code handling strict enterprise compilation warnings (Zero-Warning Standard Implementation constraint).
