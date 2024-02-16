export const colors = {
  white: '#FFFFFF',
  black: '#000000',
  light: '#EAEAEA',
  dark: '#1B1B1D',
  darker: '#0C0D0E',
  lighterGray: '#cccccc',
  lightGray: '#AAAAAA',
  gray: '#888888',
  darkGray: '#333333',
  purple: '#4F4FB0',
  orange: '#E07D13',
  yellowGold: '#F0B90B',
  red: '#CE2029',
} as const;

export type Color = (typeof colors)[keyof typeof colors];

export type ThemeColors = typeof lightColors | typeof darkColors;

export const lightColors = {
  textPrimary: colors.dark,
  textSecondary: colors.darkGray,
  backgroundPrimary: colors.light,
  backgroundSecondary: colors.white,
  backgroundTertiary: colors.lightGray,
} as const;

export const darkColors = {
  textPrimary: colors.light,
  textSecondary: colors.lightGray,
  backgroundPrimary: colors.dark,
  backgroundSecondary: colors.darker,
  backgroundTertiary: colors.darkGray,
} as const;
