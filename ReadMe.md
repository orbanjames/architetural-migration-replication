# An Empirical Study of  Architectural Migration from Blocking to Reactive Concurrency in Java Web Systems

## Overview

This repository contains the experimental implementation used in the research study:

**“An Empirical Study of incremental Architectural Migration from Blocking to Reactive Concurrency in Java Web Systems.”**

The goal of the project is to empirically evaluate the performance implications of migrating a traditional **blocking Spring-based web architecture** to a **fully reactive system**. To achieve a fair comparison, the same e-commerce REST API was implemented using three architectural styles within the same technological ecosystem.

The repository therefore contains **three functionally equivalent implementations** of the same system:

1. **MVC Blocking Architecture**

   * Traditional synchronous Spring MVC
   * Blocking I/O
   * JDBC persistence

2. **MVC Async Hybrid Architecture**

   * Spring MVC with asynchronous controllers
   * Uses `@Async` and `CompletableFuture`
   * Blocking database layer

3. **Full Reactive Architecture**

   * Spring WebFlux
   * Project Reactor
   * Non-blocking persistence using R2DBC

All three systems expose the same REST API and share identical business logic and database schema to ensure architectural differences are the primary variable.

The repository also includes:

* **Benchmark runner** used for performance testing
* **Result collection modules**
* **Benchmark configuration scripts**

---

# Repository Structure

```
project-root
│
├── mvc-blocking-arch/
│   └── Traditional blocking Spring MVC implementation
│
├── mvc-hybrid-arch/
│   └── Asynchronous MVC with CompletableFuture
│
├── Full-Reactive-arch/
│   └── Fully reactive Spring WebFlux implementation
│
├── benchmarks-runner/
│   └── Load testing and benchmarking tool
│
├── Evaluation-Results/
│   └── Benchmark output files and performance data
│
└── README.md
```

Each architecture is **self-contained** and can be executed independently.

---

# System Requirements

The experiments were conducted using the following environment.

## Required Software

| Tool             | Version | Purpose                      |
| ---------------- | ------- | ---------------------------- |
| Java JDK         | 21+     | Required for Spring Boot 3.4 |
| Gradle           | 8.x     | Build system                 |
| Spring Boot      | 3.4     | Application framework        |
| Spring Framework | 6.x     | Core framework               |
| PostgreSQL       | 15+     | Database                     |
| Git              | latest  | Repository management        |

---

# Installing the Required Tools

## 1. Install Java (JDK 21)

Spring Boot 3.4 requires Java 17+, but Java **21** is recommended.

### Ubuntu / Debian

```
sudo apt update
sudo apt install openjdk-21-jdk
```

### Verify installation

```
java -version
```

Expected output:

```
openjdk version "21"
```

---

## 2. Install Gradle

### Option 1 – Using SDKMAN (Recommended)

```
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"

sdk install gradle
```

Verify installation:

```
gradle -v
```

---

## 3. Install PostgreSQL

### Ubuntu

```
sudo apt install postgresql postgresql-contrib
```

Start the service:

```
sudo systemctl start postgresql
```

Create the database:

```
sudo -u postgres psql
```

Inside PostgreSQL:

```
CREATE DATABASE reactor_benchmark;
CREATE USER reactor_user WITH PASSWORD 'password';
GRANT ALL PRIVILEGES ON DATABASE reactor_benchmark TO reactor_user;
```

Exit:

```
\q
```

---

# Database Configuration

Each architecture uses the **same schema and dataset**.

Update the following configuration in each project:

```
src/main/resources/application.yml
```

Example configuration:

```
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/reactor_benchmark
    username: reactor_user
    password: password
```

For the **reactive implementation**, configure **R2DBC**:

```
spring:
  r2dbc:
    url: r2dbc:postgresql://localhost:5432/reactor_benchmark
    username: reactor_user
    password: password
```

---

# Building the Project

From the root of each architecture:

```
gradle clean build
```

or using the Gradle wrapper:

```
./gradlew build
```

This compiles the project and produces the executable Spring Boot JAR.

---

# Running Each Architecture

Each architecture must be run **independently** when benchmarking.

---

# 1. Running the MVC Blocking Architecture

Navigate to:

```
cd mvc-blocking
```

Build the project:

```
./gradlew build
```

Run the application:

```
./gradlew bootRun
```

or

```
java -jar build/libs/mvc-blocking.jar
```

Default port:

```
http://localhost:8080
```

---

# 2. Running the MVC Async Hybrid Architecture

Navigate to the folder:

```
cd mvc-async-hybrid
```

Build:

```
./gradlew build
```

Run:

```
./gradlew bootRun
```

or

```
java -jar build/libs/mvc-async-hybrid.jar
```

Default port:

```
http://localhost:8081
```

---

# 3. Running the Fully Reactive Architecture

Navigate to:

```
cd reactive-webflux
```

Build:

```
./gradlew build
```

Run:

```
./gradlew bootRun
```

or

```
java -jar build/libs/reactive-webflux.jar
```

Default port:

```
http://localhost:8082
```

---

# Benchmarking the Architectures

Performance evaluation is conducted using the **benchmark-runner** module.

The benchmark runner generates HTTP load and collects performance metrics including:

* Throughput
* Response time
* Latency
* Error rate
* Concurrency handling

---

# Running the Benchmark

Navigate to the benchmark module:

```
cd benchmark-runner
```

Build the benchmark runner:

```
./gradlew build
```

Run the benchmark tool:

```
./gradlew run
```

The benchmark runner will send requests to the target architecture.

Before running benchmarks:

1. Start **only one architecture**
2. Ensure the correct port is configured in the benchmark settings

Example configuration:

```
target.baseUrl=http://localhost:8080
```

---

# Benchmark Procedure

To reproduce the experimental evaluation:

### Step 1

Start **MVC Blocking**.

```
cd mvc-blocking
./gradlew bootRun
```

### Step 2

Run benchmark:

```
cd benchmark-runner
./gradlew run
```

### Step 3

Store results.

Output files will be generated in:

```
results/blocking/
```

---

Repeat the same process for the other architectures.

---

# Benchmarking the Async Hybrid System

Start:

```
mvc-async-hybrid
```

Set benchmark target:

```
http://localhost:8081
```

Run benchmark runner.

Results will be stored in:

```
results/async-hybrid/
```

---

# Benchmarking the Reactive System

Start:

```
reactive-webflux
```

Set benchmark target:

```
http://localhost:8082
```

Run benchmark runner.

Results will be stored in:

```
results/reactive/
```

---

# Collecting Benchmark Results

All benchmark outputs are automatically written to the **results directory**.

Example structure:

```
results
│
├── blocking
│   ├── throughput.csv
│   ├── latency.csv
│
├── async-hybrid
│   ├── throughput.csv
│   ├── latency.csv
│
└── reactive
    ├── throughput.csv
    ├── latency.csv
```

These files contain raw experimental data used for statistical analysis.

---

# Reproducing the Study

To reproduce the empirical evaluation:

1. Install all dependencies
2. Configure the PostgreSQL database
3. Build all architectures
4. Run each architecture independently
5. Execute the benchmark runner
6. Collect output from the `results` directory

Ensure that:

* Only **one architecture runs at a time**
* The database is reset between experiments
* Identical benchmark parameters are used for all runs

---

# Key Technologies

The following frameworks and technologies are used across the implementations:

* Spring Boot 3.4
* Spring Framework 6
* Spring MVC
* Spring WebFlux
* Project Reactor
* JDBC
* R2DBC
* PostgreSQL
* Gradle

---

# License

This project is intended for **research and academic experimentation**. Please cite the associated research paper when using the project in academic work.

---

# Contact

For questions, issues, or collaboration inquiries, please open an issue in the repository or contact the authors of the study.
