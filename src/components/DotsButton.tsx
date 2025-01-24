import React from 'react';
import {StyleSheet, Text, View} from 'react-native';
import {fontSizes} from '../constants/fontSizes';
import {colors} from '../constants/colors';

const styles = StyleSheet.create({
  container: {
    height: 30,
    width: 30,
  },
  text: {
    fontSize: fontSizes.xlarge,
    lineHeight: 30,
    color: colors.lightGray,
    textAlign: 'center',
  },
});

export const DotsButton = () => {
  return (
    <View style={styles.container}>
      <Text style={styles.text}>⋯</Text>
    </View>
  );
};
