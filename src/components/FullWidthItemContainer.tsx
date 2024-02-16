import React from 'react';
import {StyleSheet, View} from 'react-native';

import {colors} from '../constants/colors';
import {useTheme} from '../hooks/useTheme';

const styles = StyleSheet.create({
  container: {
    borderWidth: 1,
    padding: 10,
    borderRadius: 5,
    marginBottom: 10,
    display: 'flex',
    gap: 10,
  },
  containerDark: {
    borderColor: colors.darkGray,
    backgroundColor: colors.darker,
  },
  containerLight: {
    borderColor: colors.lightGray,
    backgroundColor: colors.lighterGray,
  },
});

interface FullWidthItemContainerProps {
  children: React.ReactNode;
}

export const FullWidthItemContainer: React.FC<FullWidthItemContainerProps> = ({
  children,
}) => {
  const {isDark} = useTheme();
  const containerColorStyle = isDark
    ? styles.containerDark
    : styles.containerLight;

  return (
    <View style={[styles.container, containerColorStyle]}>{children}</View>
  );
};
