import * as React from 'react';
import {Appearance, Platform, Settings, StyleSheet} from 'react-native';
import {lightColors, darkColors, ThemeColors} from '../constants/colors';
import {settingsNames} from '../constants/settingsNames';

const getStyles = (colors: ThemeColors) => {
  return StyleSheet.create({
    backgroundPrimary: {
      backgroundColor: colors.backgroundPrimary,
    },
    backgroundSecondary: {
      backgroundColor: colors.backgroundSecondary,
    },
    backgroundTertiary: {
      backgroundColor: colors.backgroundTertiary,
    },
    textPrimary: {
      color: colors.textPrimary,
    },
    textSecondary: {
      color: colors.textSecondary,
    },
  });
};

export const ThemeContext = React.createContext<{
  doesUseSystemSetting: boolean;
  isDark: boolean;
  colors: any;
  toggleScheme: () => void;
  toggleDoesUseSystemSetting: () => void;
  styles: any;
}>({
  doesUseSystemSetting: true,
  isDark: false,
  colors: lightColors,
  toggleScheme: () => {},
  toggleDoesUseSystemSetting: () => {},
  styles: getStyles(lightColors),
});

interface ThemeProviderProps {
  children?: React.ReactNode;
}

export const ThemeProvider = (props: ThemeProviderProps) => {
  const colorScheme = Appearance.getColorScheme();
  const [doesUseSystemSetting, setDoesUseSystemSetting] =
    React.useState<boolean>(() =>
      Platform.OS === 'ios'
        ? Settings.get(settingsNames.useSystemColors) ?? true
        : true,
    );
  const [isDark, setIsDark] = React.useState<boolean>(() =>
    Platform.OS === 'ios'
      ? Settings.get(settingsNames.darkMode)
      : colorScheme === 'dark',
  );

  React.useEffect(() => {
    if (doesUseSystemSetting) {
      const isSchemeDark = colorScheme === 'dark';

      if (Platform.OS === 'ios') {
        Settings.set({[settingsNames.darkMode]: isSchemeDark});
      }

      setIsDark(isSchemeDark);
    }
  }, [colorScheme, doesUseSystemSetting]);

  if (Platform.OS === 'ios') {
    Settings.watchKeys(
      [settingsNames.useSystemColors, settingsNames.darkMode],
      () => {
        setDoesUseSystemSetting(
          Settings.get(settingsNames.useSystemColors) ?? false,
        );
        setIsDark(Settings.get(settingsNames.darkMode) ?? false);
      },
    );
  }

  const defaultTheme = React.useMemo(() => {
    const colors = isDark ? darkColors : lightColors;

    return {
      doesUseSystemSetting,
      isDark,
      colors,
      toggleDoesUseSystemSetting: () => {
        if (Platform.OS === 'ios') {
          Settings.set({
            [settingsNames.useSystemColors]: !doesUseSystemSetting,
          });
        }

        setDoesUseSystemSetting(!doesUseSystemSetting);
      },
      toggleScheme: () => {
        if (Platform.OS === 'ios') {
          Settings.set({[settingsNames.darkMode]: !isDark});
        }

        setIsDark(!isDark);
      },
      styles: getStyles(colors),
    };
  }, [isDark, doesUseSystemSetting, setDoesUseSystemSetting]);

  return (
    <ThemeContext.Provider value={defaultTheme}>
      {props.children}
    </ThemeContext.Provider>
  );
};

export const useTheme = () => React.useContext(ThemeContext);
