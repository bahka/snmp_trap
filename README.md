# Technical Assignment

## The original problem

We have a CLI program that determines if a given SNMP trap starts with any of the prefixes (which are specified in a YAML file) that doesn't have any system level tests. 

The program  starts by executing `.bin/oid.sh` that located in the [oids-master](src/test/resources/oids-master) folder which assumes that it runs on Linux or macOS.

It works with snpm.yaml as default configuration file with prefixes and allows to specify another file as config file with -f parameter (`bin/oid.sh -f conf.yaml`).
The list of prefixes for which to filter is fixed for a given run and will normally contain several hundred entries.

P.S.: You may need to [build](src/test/resources/oids-master/README.md) oids-master project before you run current tests.

## Purpose of the repository

This repository contains black box tests for oids-master. 

It contains some groups of tests:
- [Performance tests](src/test/kotlin/performance): 
  - ConfigWith10KPrefixesTest - config with 10_000 prefixes that tested by different combinations of oids,
  - ConfigWith1KPrefixesTest - the same as previous but with a config with 1K prefixes.
- [Functional tests](src/test/kotlin/functional)
  - App launching tests: [AppOptionsTest](src/test/kotlin/functional/AppOptionsTest)
  - App with default config tests: [AppOptionsTest](src/test/kotlin/functional/DefaultYamlFileTest)
  - App with custom config tests: [AppOptionsTest](src/test/kotlin/functional/CustomYamlFileTest)

## Requirements 

To run tests you need to install [Kotlin](https://kotlinlang.org/docs/command-line.html) and [Java 11](https://www.codejava.net/java-se/download-and-install-java-11-openjdk-and-oracle-jdk).
The easiest way to do that is to use the [SDKMAN!](https://sdkman.io/).


- `brew install kotlin`

To check the test report you need to install [Allure](https://docs.qameta.io/allure/):
- for [linux](https://docs.qameta.io/allure/#_linux)
- for [macos](https://docs.qameta.io/allure/#_mac_os_x)

## How to run tests

To run all tests use this command:
* `./gradlew clean tests`

To run tests for the specific suit of tets use -D SUITS parameter
* `./gradlew clean tests -D SUITS=regression`

To build an Allure report for test results and open in the browser you need to add `allureServe`:
* `./gradlew clean tests allureServe`

## What can be improved
- I skipped some coverage for the app launching, and some other cases,
- Performance tests data prepared that way that allows collect durations for each case and compare them run to run, for example in [Grafana](https://grafana.com/blog/2018/11/29/pro-tips-using-grafana-in-quality-assurance/),
- I didn't spend much time to read about domain (snmp, oids), so I suppose some reasonable cases are missed,
- I didn't analyze much performance tests: I have no answer do we need a warm-up the app before tests, how much we can trust this data
- Allure steps could be improved, some actions right now not marked as @Steps and aren't presented in the Test Report.
- By some reason these tests could be Flaky - instead of output it shows `empty string`, since I never worked with CLI from code I need more time to figure out how we should wait the output,
- I don't have clean up for custom configs (I expect that it will be run on CI tool and cleaned up by it).