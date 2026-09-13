# Hill Rush — Android APK build

## 1. Install Godot 4.x
Install Godot 4.x and open this folder.

## 2. Configure Android
In Godot: Editor > Manage Export Templates, install the Android export templates.
Then Editor > Editor Settings > Export > Android:
- Android SDK path
- Java SDK path
- Debug keystore (Godot can create/use one for testing)

## 3. Export APK
Project > Export > Android APK > Export Project.
The included `export_presets.cfg` targets:
`build/HillRush.apk`

For a test APK, unsigned/debug export is fine. For Google Play, create a release keystore and sign the build.

## 4. Common errors
- "No export template": install Android export templates.
- "Android SDK not found": set Android SDK path.
- "JDK not found": set Java SDK/JDK path compatible with your Godot version.
- Google/Drive features: Android OAuth configuration must be completed separately.
- Real multiplayer/voice chat/marketplace: requires a production backend; this prototype does not create a real game server.

## Important
This package is prepared for Android export, but this environment cannot run the Godot Android compiler/export templates, so the final `.apk` is not generated here.
