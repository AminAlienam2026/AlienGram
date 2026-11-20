# AlienGram Android starter

![Android CI](../../actions/workflows/android.yml/badge.svg)

Native Android project (not a PWA) with a Telegram-inspired chat list, demo chat window, local demo Stars balance and admin menu to grant/revoke demo Premium.

**Important:** this is an early UI prototype. Messages and Stars are local in-memory demo data only; no real accounts, backend, realtime delivery, or server-backed admin authority yet. Those are next milestones.

## Build locally

Open the project in Android Studio and sync Gradle. If Android Studio asks for SDK components, install Android SDK 35 and use JDK 17/21. Build via Build > Build APKs.

## Build on GitHub

Every push / pull request runs `.github/workflows/android.yml` and uploads a debug APK:

1. Open the **Actions** tab, pick the latest run.
2. Download **AlienGram-debug-apk** from *Artifacts*.

To publish a downloadable release, push a version tag:

```bash
git tag v0.1.0
git push origin v0.1.0
```

The APK is then attached to the GitHub **Releases** page automatically.
