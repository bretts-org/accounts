# Accounts

[![linux](https://img.shields.io/circleci/project/bretts-org/accounts/master.svg?label=linux)](https://circleci.com/gh/bretts-org/accounts)
[![macOS](https://img.shields.io/travis/bretts-org/accounts/master.svg?label=macOS)](https://travis-ci.org/bretts-org/accounts)
[![windows](https://img.shields.io/appveyor/ci/aebrett/accounts/master.svg?label=windows)](https://ci.appveyor.com/project/aebrett/accounts)
[![VersionEye](https://img.shields.io/versioneye/d/user/projects/577f5aad5bb13900493de5bf.svg)](https://www.versioneye.com/user/projects/577f5aad5bb13900493de5bf)
[![Codacy grade](https://img.shields.io/codacy/grade/0240d5e9efa44dd9a684a052511ab7e5/master.svg)](https://www.codacy.com/app/aebrett/accounts)
[![Codecov](https://img.shields.io/codecov/c/github/bretts-org/accounts/master.svg)](https://codecov.io/gh/bretts-org/accounts)
[![GitHub release](https://img.shields.io/github/release/bretts-org/accounts.svg)](https://github.com/bretts-org/accounts/releases/latest)

Accounting software for the [Cortijo Rosario](http://www.cortijo-rosario.com).

### Features

* View existing transactions
* View P/L by category/month
* View P/L by period, including brought forward and year to date totals
* Transaction and P/L filtering by account type, date range and transaction type
* Add new transactions

## Install from binary

### Prerequisites
* [Java Runtime (JRE)](https://java.com/en/download/) (1.8.0_91 or greater)

### Instructions
* Download and install *Accounts.msi* from the [latest accounts release](https://github.com/bretts-org/accounts/releases/latest)
* Backup your existing transaction file
* From the installation directory (normally `C:\Program Files (x86)\Accounts`), run:<br/>
  `bin\accounts.bat "--transfile=C:\path\to\TRANS"`

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
