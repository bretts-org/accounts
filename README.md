# Accounts

[![Travis](https://img.shields.io/travis/bretts-org/accounts.svg?label=linux)](https://travis-ci.org/bretts-org/accounts)
[![AppVeyor](https://img.shields.io/appveyor/ci/aebrett/accounts.svg?label=windows)](https://ci.appveyor.com/project/aebrett/accounts)
[![Codacy grade](https://img.shields.io/codacy/grade/0240d5e9efa44dd9a684a052511ab7e5.svg)](https://www.codacy.com/app/aebrett/accounts)
[![Codecov](https://img.shields.io/codecov/c/github/bretts-org/accounts.svg)](https://codecov.io/gh/bretts-org/accounts)
[![GitHub release](https://img.shields.io/github/release/bretts-org/accounts.svg)](https://github.com/bretts-org/accounts/releases/latest)

Accounting software for the [Cortijo Rosario](http://www.cortijo-rosario.com/), with the following features:
* View existing transactions
* View P/L by category/month
* View P/L by period, including brought forward and year to date totals
* Transaction and P/L filtering by account type, date range and transaction type
* Add new transactions

## Install from binary

### Prerequisites
* [Java Runtime (JRE)](https://java.com/en/download/) (1.8.0_91 or greater)

### Instructions
* Download and install the [latest accounts release](https://github.com/bretts-org/accounts/releases/latest)
* From the installation directory, run: `accounts.bat "--transfile=C:\path\to\TRANS"`

## Build/run from source

### Prerequisites
* [Java Development Kit (JDK)](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) (1.8.0_92 or greater)
* [Scala Build Tool (sbt)](http://www.scala-sbt.org/download.html) (0.13.9 or greater)

### Instructions
* [Download](https://github.com/bretts-org/accounts/archive/dev.zip) and unzip the latest source
* From the root directory of the extracted source, run: `sbt "run --transfile=C:/path/to/TRANS"`

## Contribute

### Prerequisites
* [Java Development Kit (JDK)](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) (1.8.0_92 or greater)
* [Scala Build Tool (sbt)](http://www.scala-sbt.org/download.html) (0.13.9 or greater)
* [Git](https://git-scm.com/downloads) version control software

### Recommended
* [IntelliJ Community Edition](https://www.jetbrains.com/idea/download/#section=windows) (graphical development environment)
* [Sourcetree](https://www.sourcetreeapp.com/download) (graphical version control)

