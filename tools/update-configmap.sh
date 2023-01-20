###########################################################
#ENV VARS
###########################################################
envValue=$1
APP_NAME=$2
OPENSHIFT_NAMESPACE=$3
SPLUNK_TOKEN=$4

SPLUNK_URL="gww.splunk.educ.gov.bc.ca"
FLB_CONFIG="[SERVICE]
   Flush        1
   Daemon       Off
   Log_Level    info
   HTTP_Server   On
   HTTP_Listen   0.0.0.0
   Parsers_File parsers.conf
[INPUT]
   Name   tail
   Path   /mnt/log/*
   Exclude_Path *.gz,*.zip
   Parser docker
   Mem_Buf_Limit 20MB
[FILTER]
   Name record_modifier
   Match *
   Record hostname \${HOSTNAME}
[OUTPUT]
   Name   stdout
   Match  *
[OUTPUT]
   Name  splunk
   Match *
   Host  $SPLUNK_URL
   Port  443
   TLS         On
   TLS.Verify  Off
   Message_Key $APP_NAME
   Splunk_Token $SPLUNK_TOKEN
"
PARSER_CONFIG="
[PARSER]
    Name        docker
    Format      json
"
###########################################################
#Setup for config-maps
###########################################################
echo Creating config map "$APP_NAME"-config-map
oc create -n "$OPENSHIFT_NAMESPACE"-"$envValue" configmap "$APP_NAME"-config-map \
 --from-literal=APP_LOG_LEVEL="INFO" \
 --from-literal=ENABLE_FLYWAY="true" \
 --from-literal=GRAD_PROGRAM_API="http://educ-grad-program-api.$OPENSHIFT_NAMESPACE-$envValue.svc.cluster.local:8080/" \
 --from-literal=GRAD_TRAX_API="http://educ-grad-trax-api.$OPENSHIFT_NAMESPACE-$envValue.svc.cluster.local:8080/" \
 --dry-run=client -o yaml | oc apply -f -
echo

echo Creating config map "$APP_NAME"-flb-sc-config-map
oc create -n "$OPENSHIFT_NAMESPACE"-"$envValue" configmap "$APP_NAME"-flb-sc-config-map \
  --from-literal=fluent-bit.conf="$FLB_CONFIG" \
  --from-literal=parsers.conf="$PARSER_CONFIG" \
  --dry-run=client -o yaml | oc apply -f -

