import * as React from 'react';
import {TouchableOpacity, StyleSheet, Text, View, Linking} from 'react-native';

const styles = StyleSheet.create({
  madeBy: {
    paddingVertical: 7,
    paddingHorizontal: 20,
    flexDirection: 'row',
    justifyContent: 'flex-end',
  },
  text: {
    fontSize: 12,
    color: '#333333',
  },
  link: {
    color: '#e07d13',
  },
  heart: {
    color: '#ce2029',
  },
});

const handleLinkPress = (): void => {
  Linking.openURL('https://oranger.co.uk');
};

export const MadeBy: React.FC = () => (
  <View style={styles.madeBy}>
    <Text style={styles.text}>Made with </Text>
    <Text style={StyleSheet.flatten([styles.text, styles.heart])}>
      &#9829;{' '}
    </Text>
    <Text style={styles.text}>by </Text>
    <TouchableOpacity onPress={handleLinkPress}>
      <Text style={StyleSheet.flatten([styles.text, styles.link])}>
        oranger
      </Text>
    </TouchableOpacity>
  </View>
);
