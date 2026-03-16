# AI Blast Radius Analyzer Plugin

[![Build Status](https://img.shields.io/badge/build-passing-brightgreen)](#)
[![Version](https://img.shields.io/badge/version-1.0--SNAPSHOT-blue)](#)
[![IntelliJ Platform](https://img.shields.io/badge/IntelliJ-2023.2%2B-blueviolet)](#)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

**AI Blast Radius Analyzer** is an enterprise-grade IntelliJ IDE plugin that empowers engineers to perform realtime impact analyses on Java classes. Using real-time static dependency evaluation mixed with Deep Git History insights, this plugin predicts how many downstream and transitive files will be affected when a class is modified.

## 🚀 Features

* **Real-time Gutter Indicators**: Visual color-coded severity icons located securely beside your class definitions indicating risk before you make modifications.
* **Deep Code Insight**: Combines IntelliJ PSI (Program Structure Interface) rules to track **Direct** and **Transitive (up to 3 levels deep)** dependencies.
* **Intelligent Git Coupling Analysis**: Checks up to 6 months of Git history using the `Git4Idea` and `intellij.vcs.git` ecosystem to map *Change Coupling* (files heavily changed concurrently).
* **Rich Popups**: On-hover graphical overlays that detail specific dependency risks and overall impact thresholds.
* **On-Demand Context Action**: Triggering context-menu (Right Click -> "Analyze Blast Radius") provides detailed readouts formatted perfectly for your local IDE environment.

## 🛠️ Tech Stack & Architecture

* **Core Language:** Java / Kotlin (Build Script).
* **Environment framework:** JetBrains IntelliJ Platform SDK.
* **Code Interrogation Framework:** `com.intellij.psi` engine for semantic tree searches.
* **VCS Integration Framework:** `git4idea` API leveraging local indexing mechanisms natively shipped with JetBrains.
* **Enterprise Testing Setup:** JUnit 5 (Platform) paired seamlessly with JUnit Jupiter + Mockito frameworks ensuring deterministic build runs internally.

### 📐 Threshold Models / Severity

Calculations map overall cumulative modifications using risk severity bands:

| Tier         | Bounds           | Indicator Color      |
|:------------:|:----------------:|:--------------------:|
| **LOW**      | `< 5 changes`    | 🟢 Green           |
| **MEDIUM**   | `5 - 15 changes` | 🟡 Yellow          |
| **HIGH**     | `15 - 30 changes`| 🟠 Orange          |
| **CRITICAL** | `> 30 changes`   | 🔴 Red             |

## ⚙️ Requirements

* **IntelliJ IDEA** 2023.2.5 (`232.*`) or newer (tested heavily internally on macOS/Linux/Win configurations).
* **JDK 17 Runtime Environment** required for build.

## 🏗️ Building & Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/gajjela521/ai-blast-radius-plugin.git
   cd ai-blast-radius-plugin
   ```

2. **Verify Java Version (Needs version 17+ via Gradle 8.5+ JVM tooling):**
   ```bash
   export JAVA_HOME=/path/to/your/jdk17/home
   ```

3. **Assemble the ZIP Payload:**
   ```bash
   ./gradlew clean buildPlugin
   ```

4. **Install Locally inside IntelliJ:**
   * Navigate to `Preferences (Settings)` -> `Plugins`
   * Click the ⚙️ Setup icon -> `Install Plugin from Disk...`
   * Select `/ai-blast-radius-plugin/build/distributions/ai-blast-radius-plugin-1.0-SNAPSHOT.zip`
   * **Restart IntelliJ.**

## 🧪 Testing Guidelines (Enterprise Tier)

Our platform operates fully within bounded test isolation utilizing IntelliJ's headless framework alongside Mockito:
```bash
./gradlew clean test
```
The robust suite maps `BlastRadiusCalculator` and `BlastRadiusResult` objects against bounded dependency tests, ensuring enterprise continuity independent from graphical X11 instances.

## 🤝 Contributing
Interested in adding expanded multi-language scopes (like Python/Kotlin PSI scanning) or fine-tuning ML/AI metric evaluations? Check out [CONTRIBUTING.md](CONTRIBUTING.md) to kickstart your journey building alongside us!

## 📜 License
Distributable under the [MIT License](LICENSE).
