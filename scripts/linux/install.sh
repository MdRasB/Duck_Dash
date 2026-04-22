#!/bin/bash

set -e  # stop on error

APP_NAME="duckdash"

echo "🚀 Starting full deployment..."

# -------------------------------
# 1. BUILD PROJECT
# -------------------------------
echo "🔧 Running build script..."
chmod +x scripts/linux/build.sh
./scripts/linux/build.sh

# -------------------------------
# 2. CHECK EXISTING INSTALLATION
# -------------------------------
echo "🔍 Checking existing installation..."

# Check if installed
if dpkg -l | grep -q "^ii  $APP_NAME "; then
    echo "⚠️ Found existing installation of $APP_NAME"

    echo "🗑 Removing old version..."
    sudo dpkg -r $APP_NAME

    # Optional: remove config files too (uncomment if needed)
    # sudo dpkg -P $APP_NAME

    echo "✅ Old version removed."
else
    echo "✔ No previous installation found."
fi

# Find latest .deb file
echo "📦 Searching for .deb package..."
DEB_FILE=$(ls -t target/${APP_NAME}_*.deb 2>/dev/null | head -n 1)

if [ -z "$DEB_FILE" ]; then
    echo "❌ No .deb file found in target/"
    exit 1
fi

echo "📦 Installing: $DEB_FILE"

# Install new package
sudo dpkg -i "$DEB_FILE"

# Fix dependencies if needed
sudo apt-get install -f -y

echo "✅ Installation complete!"
echo "🚀 Run your game using: /opt/duckdash/bin/DuckDash"