# VoloX Launcher 1.0

A starter native Android launcher for Android 8.0+ with a blue-and-black VoloX theme.

## What is included
- HOME launcher activity so Android can offer it as a default Home app
- App drawer listing launchable apps
- App search
- Clock/date home screen
- Shortcuts to Android Wi-Fi and Settings screens
- GitHub Actions workflow to build a debug APK in the cloud

## Build in GitHub using only an Android phone

1. Create a GitHub repository named `VoloX-Launcher`.
2. Upload the contents of this project ZIP into the repository (upload the files and folders inside the ZIP, not the ZIP itself).
3. Make sure `.github/workflows/android.yml` is present on the `main` branch.
4. Open the repository's **Actions** tab.
5. Select **Build VoloX Launcher APK** and tap **Run workflow**. A push to `main` also starts a build.
6. When the run finishes, open it and find **Artifacts**.
7. Download `VoloX-Launcher-1.0-debug`, extract it, and install `app-debug.apk`.

The first build may take several minutes. GitHub may ask you to verify your account or enable Actions.

## Set it as the Home app

After installing:
1. Press the phone's Home button.
2. If Android asks which Home app to use, choose **VoloX OS**.
3. Choose **Always** only after checking that it works.
4. To switch back, open Android Settings > Apps > Default apps > Home app and choose the original Realme launcher. Menu wording can vary by Realme UI version.

## Important limitations of version 1.0

This is a launcher app, not replacement firmware or a complete Android OS. It does not replace Realme UI, the kernel, notification shade, lock screen, or system settings. The VoloX AI tile is currently a visual placeholder. App icons are simple letter tiles in this starter build.

Keep the original launcher installed. This debug APK is for testing and is not a signed release build.
