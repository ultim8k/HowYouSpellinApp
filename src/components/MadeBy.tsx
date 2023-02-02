import * as React from 'react';
import {TouchableOpacity, StyleSheet, Text, View, Linking} from 'react-native';

const styles = StyleSheet.create({
  madeBy: {
    paddingVertical: 10,
    paddingHorizontal: 20,
    flexDirection: 'row',
    justifyContent: 'center',
    borderBottomWidth: 1,
    borderBottomColor: '#999999',
    marginBottom: 20,
    marginHorizontal: 20,
  },
  text: {
    fontSize: 14,
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
  Linking.openURL('https://kostas.rocks');
};

export const MadeBy: React.FC = () => (
  <View style={styles.madeBy}>
    <Text style={styles.text}>Made with </Text>
    <Text style={StyleSheet.flatten([styles.text, styles.heart])}>
      &#9829;{' '}
    </Text>
    <Text style={styles.text}>by </Text>
    <TouchableOpacity onPress={handleLinkPress}>
      <Text style={StyleSheet.flatten([styles.text, styles.link])}>Kostas</Text>
    </TouchableOpacity>
  </View>
);
