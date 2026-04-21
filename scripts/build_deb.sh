#!/usr/bin/env bash
set -euo pipefail

# Builds a Debian installer (.deb) that includes the intro video as external app payload.
# This avoids JavaFX Media trying (and failing) to play video directly from the runtime image.

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "${ROOT_DIR}"

APP_NAME="duck-dash"
APP_VERSION="$(sed -n 's:.*<version>\([^<]*\)</version>.*:\1:p' pom.xml | head -n 1)"
MAIN_MODULE="edu.bauet.java.cse.duckrun/edu.bauet.java.cse.duckrun.MainApp"
RUNTIME_IMAGE="target/duck-dash"
DEST_DIR="target/installer"

if [[ -z "${APP_VERSION}" ]]; then
  echo "Could not determine <version> from pom.xml" >&2
  exit 1
fi

# 1) Build a runtime image (jlink) via the JavaFX Maven plugin.
mvn -q -DskipTests clean compile javafx:jlink

# 2) Build the .deb using that runtime image and add Story/ as external payload.
rm -rf "${DEST_DIR}"
mkdir -p "${DEST_DIR}"

jpackage \
  --type deb \
  --dest "${DEST_DIR}" \
  --name "${APP_NAME}" \
  --app-version "${APP_VERSION}" \
  --module "${MAIN_MODULE}" \
  --runtime-image "${RUNTIME_IMAGE}" \
  --app-content "src/main/resources/Story"\
  --linux-shortcut

echo "Built: ${DEST_DIR}/${APP_NAME}_${APP_VERSION}_amd64.deb"
