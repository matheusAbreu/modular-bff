set -e

JVM_OPS="${JVM_OPS:-""}"
COMMAND=${1:-"dev"}
echo $COMMAND

case "$COMMAND" in
  dev)
    exec java $JVM_OPS -jar quarkus-*.jar \
         -Dquarkus.http.host=0.0.0.0 \
         -Dquarkus-args= "$RUN_MODULE" \
          "$COMMAND"
    ;;
  *)
    exec sh -c "$*"
    ;;
esac