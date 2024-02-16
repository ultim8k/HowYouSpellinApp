import React from 'react';
import {Text, View, StyleSheet} from 'react-native';
import {useTheme} from '../hooks/useTheme';

const styles = StyleSheet.create({
  text: {
    textAlign: 'center',
    marginBottom: 20,
  },
});

export const Intro = () => {
  const {styles: themeStyles} = useTheme();

  return (
    <View>
      <Text style={[styles.text, themeStyles.textPrimary]}>
        Convert text to spell words using the International Radiotelephony
        Spelling Alphabet.
      </Text>
    </View>
  );
};
