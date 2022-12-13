import React from 'react';
import {StyleSheet, Switch, Text, TextInput, View} from 'react-native';

import {useListOrientation} from '../hooks/useListOrientation';

const styles = StyleSheet.create({
  textInput: {
    padding: 10,
    marginBottom: 15,
    backgroundColor: '#ffffff',
  },
  checkboxLine: {
    marginBottom: 20,
    flexDirection: 'row',
    justifyContent: 'space-between',
  },
  formWrapper: {
    padding: 20,
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

  return (
    <View style={styles.formWrapper}>
      <TextInput
        style={styles.textInput}
        placeholder="Type some text"
        defaultValue={text}
        onChangeText={handleTextChange}
      />
      <View style={styles.checkboxLine}>
        <Text>Display Inline?</Text>
        <Switch onValueChange={changeListOrientation} value={isHorizontal} />
      </View>
    </View>
  );
};
