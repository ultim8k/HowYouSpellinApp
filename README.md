# How you spellin app

Convert text to spell words using the "International Radiotelephony Spelling Alphabet".

> Created with ❤️ using ReactNative™️

![iTunes App Store](https://img.shields.io/itunes/v/1663502643?style=for-the-badge&logo=ios&logoColor=black&label=Install&link=https%3A%2F%2Fapps.apple.com%2Fgb%2Fapp%2Fhow-you-spellin%2Fid1663502643)

![iOS screenshot](https://user-images.githubusercontent.com/701815/207428158-04f11626-ac77-4091-bdb5-2970bf632de1.png)

## Requirements

- Node.js 22.11 or newer (React Native 0.88)
- Xcode (with the iOS Simulator) for iOS
- Ruby and Bundler, for CocoaPods

## Getting started

```bash
npm install
cd ios && bundle install && bundle exec pod install && cd ..
npm start        # in one terminal: Metro
npm run ios      # in another: build and run in the simulator
```

Xcode runs a script phase that needs a recent Node. If the build fails at
"Bundle React Native code and images", point Xcode at the right Node by creating
`ios/.xcode.env.local` (gitignored):

```bash
export NODE_BINARY=/path/to/node
```

To run on a device, open `ios/HowYouSpellinApp.xcworkspace` in Xcode, select the
HowYouSpellinApp target, then _Signing & Capabilities_, and choose your team.
Enable Developer Mode on the device first.

Android has not been updated since the React Native 0.88 upgrade and is not
currently built or published.

## Scripts

| Command            | What it does                         |
| ------------------ | ------------------------------------ |
| `npm start`        | Start Metro                          |
| `npm run ios`      | Build and run on the iOS simulator   |
| `npm test`         | Run the unit and render tests (Jest) |
| `npm run lint`     | Lint `src` and `__tests__`           |
| `npx tsc --noEmit` | Type-check                           |

## Testing

- **Automated:** `npm test`. Unit tests cover the character mapping and helpers;
  render tests drive the real app (typing, orientation toggle, About links).
  See [docs/TESTING.md](docs/TESTING.md).
- **Manual:** run through [docs/MANUAL_TEST_CHECKLIST.md](docs/MANUAL_TEST_CHECKLIST.md)
  on a TestFlight build before submitting to the App Store.

## Releasing

See [docs/RELEASING.md](docs/RELEASING.md).

## TODO

- Ship to Play Store (Android needs the same React Native upgrade first)
- Add favourites
- Add GPS reading to spell out location on emergencies
- Add automated screenshots with Detox and fastlane
