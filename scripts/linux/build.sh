#!/bin/bash

set -e  # Stop on any error

# -------------------------------
# AUTO CONFIG FROM POM
# -------------------------------
APP_NAME="DuckDash"
MODULE_NAME="edu.bauet.java.cse.duckrun"
MAIN_CLASS="edu.bauet.java.cse.duckrun.MainApp"

# Get version dynamically from pom.xml
VERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)
JAR_NAME="duckrun-${VERSION}.jar"

echo "🚀 Building ${APP_NAME} v${VERSION}..."

# -------------------------------
# 1. BUILD PROJECT
# -------------------------------
echo "📦 Running Maven build..."
mvn clean package

# -------------------------------
# 2. PREPARE APP DIRECTORY
# -------------------------------
echo "📁 Preparing app directory..."

rm -rf target/app
mkdir -p target/app

cp target/${JAR_NAME} target/app/
cp target/deps/* target/app/

# -------------------------------
# 3. CREATE RUNTIME (jlink)
# -------------------------------
echo "⚙️ Creating custom runtime..."
rm -rf target/runtime

jlink \
  --module-path "$JAVA_HOME/jmods:target/deps:target/${JAR_NAME}" \
  --add-modules ${MODULE_NAME} \
  --output target/runtime \
  --strip-debug \
  --no-header-files \
  --no-man-pages \
  --compress=2

# -------------------------------
# 4. BUILD INSTALLER (jpackage)
# -------------------------------
echo "📦 Creating .deb package..."
rm -rf target/${APP_NAME}*

jpackage \
  --type deb \
  --name ${APP_NAME} \
  --app-version ${VERSION} \
  --input target/app \
  --main-jar ${JAR_NAME} \
  --main-class ${MAIN_CLASS} \
  --runtime-image target/runtime \
  --dest target \
  --resource-dir src/packaging/linux \
  --linux-shortcut \
  --linux-menu-group "Game"

# -------------------------------
# DONE
# -------------------------------
echo "✅ Build complete!"
echo "📍 Output: target/${APP_NAME}_${VERSION}_amd64.deb"