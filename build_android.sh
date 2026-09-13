#!/usr/bin/env bash
set -e
mkdir -p build
godot --headless --path . --export-debug "Android APK" build/HillRush.apk
echo "Created build/HillRush.apk"
