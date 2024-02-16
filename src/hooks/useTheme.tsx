import * as React from 'react';
import {Appearance, StyleSheet} from 'react-native';
import {lightColors, darkColors, ThemeColors} from '../constants/colors';

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
    React.useState<boolean>(true);
  const [isDark, setIsDark] = React.useState(colorScheme === 'dark');

  React.useEffect(() => {
    if (doesUseSystemSetting) {
      setIsDark(colorScheme === 'dark');
    }
  }, [colorScheme, doesUseSystemSetting]);

  const defaultTheme = React.useMemo(() => {
    const colors = isDark ? darkColors : lightColors;

    return {
      doesUseSystemSetting,
      isDark,
      colors,
      toggleDoesUseSystemSetting: () => {
        setDoesUseSystemSetting(!doesUseSystemSetting);
      },
      toggleScheme: () => setIsDark(!isDark),
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
