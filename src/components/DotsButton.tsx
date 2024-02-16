import React from 'react';
import {StyleSheet, Text, View} from 'react-native';
import {fontSizes} from '../constants/fontSizes';
import {colors} from '../constants/colors';

const styles = StyleSheet.create({
  container: {},
  text: {
    fontSize: fontSizes.xlarge,
    color: colors.lightGray,
  },
});

export const DotsButton = () => {
  return (
    <View style={styles.container}>
      <Text style={styles.text}>⋯</Text>
    </View>
  );
};
