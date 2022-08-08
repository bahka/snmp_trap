# OID App
A command-line application that determines if an OID is a descendant of another OID.

These ancestor OIDs (or "prefix" OIDs) are specified in a configuration file.

## Prerequisites
The Java JDK/JRE (minimum version 8) must be installed.

## Building and Deploying
Using the supplied Gradle wrapper script:
```
$ ./gradlew build deploy
```

Note: unit tests are executed as part of the 'build' task.

## Usage

### Configuration
This application requires a YAML document containing the ancestor ("prefix") OIDs.

The following structure is required:
```
trap-type-oid-prefix:
  - .1.3.6.1.6.3.1.1.5
  - .1.3.6.1.2.1.10.166.3
```

The document must contain a key named 'trap-type-oid-prefix' which holds a list of OIDs.

### Running
After building/deploying, run the helper script to launch the application.

The YAML document may be specified with option "-f".

If not provided, the application will default to a YAML document with name 'snmp.yaml' in the project's 
root directory.

```
$ ./bin/oid.sh
```

### Example Usage

```
$ ./bin/oid.sh
Enter an OID to process.
Enter 'quit' to exit at any time.


.1.3.6.1.2.1.10.32.0.1.5.4.3.2.1
.1.3.6.1.2.1.10.32.0.1.5.4.3.2.1: true

.1.3.6.1.2.1.10.32.0.1   
.1.3.6.1.2.1.10.32.0.1: true

.1.3.6.1 
.1.3.6.1: false

.1.3.6.1.2.1.10.55     
.1.3.6.1.2.1.10.55: false

quit
Exiting.
$
```
