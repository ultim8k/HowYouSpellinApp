# Releasing to the App Store

1. **Bump the version** in `package.json` and in Xcode (`MARKETING_VERSION`).
   `CURRENT_PROJECT_VERSION` (the build number) must be higher than any build
   already uploaded for that version.
2. **Check**: `npm test`, `npm run lint`, `npx tsc --noEmit`.
3. **Archive**:
   ```bash
   cd ios
   xcodebuild -workspace HowYouSpellinApp.xcworkspace -scheme HowYouSpellinApp \
     -configuration Release -destination 'generic/platform=iOS' \
     -archivePath build/HowYouSpellinApp.xcarchive -allowProvisioningUpdates archive
   ```
   Or use _Product → Archive_ in Xcode.
4. **Upload** to App Store Connect (needs the Apple ID signed in to Xcode):
   ```bash
   xcodebuild -exportArchive -archivePath build/HowYouSpellinApp.xcarchive \
     -exportOptionsPlist build/ExportOptions.plist -exportPath build/export \
     -allowProvisioningUpdates
   ```
   with `build/ExportOptions.plist` containing `method` = `app-store-connect`,
   `destination` = `upload`, `teamID` = `6JBV7F73AN` and `signingStyle` = `automatic`.
   Or use _Distribute App_ in the Xcode Organizer.
5. Wait for the "build has completed processing" email.
6. **TestFlight**: install it and run
   [MANUAL_TEST_CHECKLIST.md](MANUAL_TEST_CHECKLIST.md).
7. **Submit** the tested build for review from the version page in App Store Connect.

## Notes

- Warnings about missing dSYMs for `React`, `ReactNativeDependencies` and
  `hermesvm` are expected; the prebuilt frameworks do not ship them.
- Details: bundle id `rocks.kostas.native.app.HowYouSpellinApp`, team
  `6JBV7F73AN`, App Store ID `1663502643`, minimum iOS 15.1.
- Apple can remove apps it considers stale (notice received Oct/Nov 2026, with 90
  days to ship an update), so keep the app building on current Xcode.
