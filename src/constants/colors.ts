export const colors = {
  white: '#FFFFFF',
  black: '#000000',
  light: '#EAEAEA',
  dark: '#1B1B1D',
  darker: '#0C0D0E',
  lightGray: '#AAAAAA',
  gray: '#888888',
  darkGray: '#333333',
  purple: '#4F4FB0',
  orange: '#E07D13',
  red: '#CE2029',
} as const;

export type Color = (typeof colors)[keyof typeof colors];
