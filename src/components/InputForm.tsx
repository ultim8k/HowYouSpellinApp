import React from 'react';
import {StyleSheet, TextInput, View} from 'react-native';

import {colors} from '../constants/colors';
import {AddToFavouritesButton} from './AddToFavouritesButton';
import {useTheme} from '../hooks/useTheme';

const styles = StyleSheet.create({
  inputContainer: {
    borderRadius: 3,
    flexDirection: 'row',
    alignItems: 'center',
    paddingRight: 7,
    gap: 3,
  },
  inputContainerDark: {
    backgroundColor: colors.darker,
  },
  inputContainerLight: {
    backgroundColor: colors.white,
  },
  textInput: {
    padding: 10,
    paddingRight: 3,
    flex: 1,
  },
  textInputDark: {
    color: colors.light,
  },
  textInputLight: {
    color: colors.black,
  },
  textDark: {
    color: colors.light,
  },
  textLight: {
    color: colors.black,
  },
});

const getSchemeColorStyles = (isDark: boolean) => {
  return {
    text: isDark ? styles.textInputDark : styles.textInputLight,
    container: isDark ? styles.inputContainerDark : styles.inputContainerLight,
  };
};

interface InputFormProps {
  text: string;
  handleTextChange: (text: string) => void;
}

export const InputForm: React.FC<InputFormProps> = ({
  text,
  handleTextChange,
}) => {
  const {isDark} = useTheme();
  const schemeStyles = getSchemeColorStyles(isDark);

  return (
    <View style={[styles.inputContainer, schemeStyles.container]}>
      <TextInput
        style={[styles.textInput, schemeStyles.text]}
        placeholder="Type some text"
        placeholderTextColor={colors.gray}
        defaultValue={text}
        onChangeText={handleTextChange}
        autoFocus={true}
        autoComplete="name"
        // clearButtonMode="while-editing" // iOS only - perhaps need to move the favourite button somewhere else
      />
      <AddToFavouritesButton text={text} />
    </View>
  );
};
