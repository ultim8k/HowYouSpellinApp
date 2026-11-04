# Manual test checklist

Run on a **TestFlight build** (Release configuration), not a debug build.
Cover at least one iPhone and one iPad, on the latest iOS plus one older
supported version if available. Tick each box and note the device and build.

**Build:** **\_\_** **Device / iOS:** **\_\_** **Tester / date:** **\_\_**

## 1. Install and launch

- [ ] Installs from TestFlight; app name reads "How you spellin?"
- [ ] App icon is correct on the home screen
- [ ] Launch screen shows "How you spellin?" (delete the app first so iOS does not show a cached one)
- [ ] App opens to the home screen with no crash or red/yellow screen
- [ ] Cold start, background and resume, and force-quit and relaunch all work

## 2. Spelling (core feature)

- [ ] The text field is focused on launch and the keyboard appears
- [ ] `hi` shows **Hotel**, **India**
- [ ] Lowercase and uppercase give the same result
- [ ] Digits: `0123456789` spell Zero to Nine
- [ ] Symbols: `-` `.` `,` `_` show dash, full stop, comma, underscore
- [ ] A space shows `- break -`, and it is greyed and full-width
- [ ] Unsupported characters (`@`, `é`, an emoji) show `N/A` without crashing
- [ ] Deleting all text clears the list
- [ ] Pasting a long text (500+ characters) stays responsive
- [ ] The first letter of each word is bold

## 3. Keyboard and scrolling (changed in 1.7.1)

- [ ] With a long text, the list scrolls and **is not hidden behind the keyboard**
- [ ] The list is squeezed between the text field and the keyboard
- [ ] The last item can be scrolled into view with the keyboard open
- [ ] Dismissing the keyboard restores the full-height list, with no gap or jump
- [ ] Same behaviour in both horizontal (wrapped) and vertical layouts
- [ ] Rotating while the keyboard is open (iPad) keeps the layout correct
- [ ] iPad with an external or floating keyboard does not break the layout

## 4. Layout toggle

- [ ] "Display words vertically?" is off by default
- [ ] Turning it on shows one word per row; turning it off wraps the words
- [ ] The text stays intact when toggling and the toggle keeps its state

## 5. About screen

- [ ] "About" opens a modal with an orange header titled "About"
- [ ] The modal can be dismissed (swipe down or back) and Home is intact
- [ ] **Web app** opens https://how-you-spell.in
- [ ] **Privacy Policy** opens https://how-you-spell.in/about/privacy-policy
- [ ] **Source code on Github** opens the repository
- [ ] "Made by" link opens kostas.rocks

## 6. Appearance

- [ ] Light mode: light background, dark text, readable placeholder
- [ ] Dark mode: dark background, light text, input field is dark
- [ ] Switching the system appearance while the app is running looks right after relaunch
- [ ] Nothing sits under the notch / Dynamic Island, status bar or home indicator
- [ ] Dynamic Type (larger text) does not clip the title or the list

## 7. iPad

- [ ] Portrait and landscape both lay out correctly
- [ ] Rotating keeps the text and scroll position
- [ ] Split View and Slide Over resize without breaking the layout

## 8. Stability and update path

- [ ] No crash in 5 minutes of mixed use
- [ ] Updating over the currently released App Store version (1.7) keeps working with no stale state
- [ ] Low Power Mode and airplane mode: the app works fully offline (only the links need a network)

## 9. Before submitting to review

- [ ] Version and build number match App Store Connect
- [ ] "What's New" text is written
- [ ] Screenshots are still accurate (the UI has not changed)
- [ ] Privacy details in App Store Connect still match what the app actually does
