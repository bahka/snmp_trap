#!/usr/bin/env bash

SCRIPT_DIR=$(dirname $0)
INPUT_FILE="$SCRIPT_DIR"/../snmp.yaml

Help () {
   cat << EOF

A program to determine whether an OID is a descendant of an OID in a YAML document.

Usage: $(basename $0) [-f <file name>] [-h]

where:
  -f  Path to the YAML document.
  -h  Shows this help.
EOF
}

#
# Main
#

which java > /dev/null 2>&1
if [ $? -ne 0 ]; then
  echo "Error: 'java' is not in PATH."
  exit 1
fi

while getopts "hf:" arg; do
  case "$arg" in
    h)
      Help
      exit 0
      ;;
    f)
      INPUT_FILE="$OPTARG"
      ;;
    *)
      Help
      exit 1
      ;;
  esac
done

java -cp $SCRIPT_DIR/../lib/*:$SCRIPT_DIR/../lib/thirdparty/* com.bouff.oids.OidAppLauncher $INPUT_FILE
