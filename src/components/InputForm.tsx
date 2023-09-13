import React from 'react';
import {
  Appearance,
  StyleSheet,
  Switch,
  Text,
  TextInput,
  View,
} from 'react-native';

import {useListOrientation} from '../hooks/useListOrientation';
import {colors} from '../constants/colors';

const styles = StyleSheet.create({
  textInput: {
    padding: 10,
    marginBottom: 15,
    borderRadius: 3,
  },
  textInputDark: {
    backgroundColor: colors.darker,
    color: colors.light,
  },
  textInputLight: {
    backgroundColor: colors.white,
    color: colors.black,
  },
  switchBoxLine: {
    marginBottom: 20,
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  switchBox: {
    transform: [{scaleX: 0.7}, {scaleY: 0.7}],
  },
  formWrapper: {
    padding: 20,
  },
  textDark: {
    color: colors.light,
  },
  textLight: {
    color: colors.black,
  },
});

interface InputFormProps {
  text: string;
  handleTextChange: (text: string) => void;
}

export const InputForm: React.FC<InputFormProps> = ({
  text,
  handleTextChange,
}) => {
  const {isHorizontal, changeListOrientation} = useListOrientation();
  const colorScheme = Appearance.getColorScheme();

  const textInputColorStyle =
    colorScheme === 'dark' ? styles.textInputDark : styles.textInputLight;
  const textColorStyle =
    colorScheme === 'dark' ? styles.textDark : styles.textLight;

  return (
    <View style={styles.formWrapper}>
      <TextInput
        style={[styles.textInput, textInputColorStyle]}
        placeholder="Type some text"
        placeholderTextColor={colors.gray}
        defaultValue={text}
        onChangeText={handleTextChange}
        autoFocus={true}
      />
      <View style={styles.switchBoxLine}>
        <Text style={textColorStyle}>Display words vertically?</Text>
        <Switch
          style={styles.switchBox}
          onValueChange={changeListOrientation}
          value={!isHorizontal}
        />
      </View>
    </View>
  );
};
