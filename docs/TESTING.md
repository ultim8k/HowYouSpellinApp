# Testing

```bash
npm test             # Jest
npm run lint
npx tsc --noEmit
```

## What is covered

| File                                        | Covers                                                                                                              |
| ------------------------------------------- | ------------------------------------------------------------------------------------------------------------------- |
| `__tests__/utils-test.ts`                   | `charsMap` completeness, `replaceCharWithSpellWord`, `isBreak`, unsupported characters                              |
| `__tests__/WordWithStrongFirstCap-test.tsx` | Word rendering, emphasised first letter, whitespace trimming                                                        |
| `__tests__/App-test.tsx`                    | Whole app: typing, digits, breaks, `N/A`, clearing, orientation toggle, long text, About screen and its three links |

## How the render tests work

They use `react-test-renderer` against the real `<App />`, including navigation,
and interact through props (`onChangeText`, `onValueChange`, `onPress`). Helpers
are in `__tests__/helpers/render.tsx`.

`jest.setup.js` mocks `react-native-safe-area-context`. Without it,
`SafeAreaProvider` renders nothing in Jest and every render test passes
vacuously, so keep it.

## What is not covered

Automated tests cannot see native behaviour. These need the manual checklist
([MANUAL_TEST_CHECKLIST.md](MANUAL_TEST_CHECKLIST.md)):

- Keyboard avoidance and layout on real devices
- Dark mode and launch screen
- iPad orientations
- Opening links in the system browser
- Install, update and signing
