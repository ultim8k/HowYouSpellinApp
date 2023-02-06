import React from 'react';
import {StyleSheet, Switch, Text, TextInput, View} from 'react-native';

import {useListOrientation} from '../hooks/useListOrientation';

const styles = StyleSheet.create({
  textInput: {
    padding: 10,
    marginBottom: 15,
    backgroundColor: '#ffffff',
    borderRadius: 3,
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
        placeholderTextColor="#777"
        defaultValue={text}
        onChangeText={handleTextChange}
        autoFocus={true}
      />
      <View style={styles.switchBoxLine}>
        <Text>Display words vertically?</Text>
        <Switch
          style={styles.switchBox}
          onValueChange={changeListOrientation}
          value={!isHorizontal}
        />
      </View>
    </View>
  );
};
