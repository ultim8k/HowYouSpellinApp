import React from 'react';
import {Linking, StyleSheet, Text, TouchableOpacity, View} from 'react-native';
import {MadeBy} from '../components/MadeBy';

const styles = StyleSheet.create({
  text: {
    textAlign: 'center',
    fontSize: 14,
    color: '#4f4fb0',
  },
  link: {
    marginBottom: 5,
  },
});

const handleWebappLinkPress = (): void => {
  Linking.openURL('https://how-you-spell.in');
};

const handleLegalLinkPress = (): void => {
  Linking.openURL('https://how-you-spell.in/about/privacy-policy');
};

const handleGithubLinkPress = (): void => {
  Linking.openURL('https://github.com/ultim8k/HowYouSpellinApp');
};

export const About: React.FC = () => {
  return (
    <View>
      <MadeBy />
      <TouchableOpacity onPress={handleWebappLinkPress}>
        <View style={styles.link}>
          <Text style={styles.text}>Web app</Text>
        </View>
      </TouchableOpacity>
      <TouchableOpacity onPress={handleLegalLinkPress}>
        <View style={styles.link}>
          <Text style={styles.text}>Privacy Policy</Text>
        </View>
      </TouchableOpacity>
      <TouchableOpacity onPress={handleGithubLinkPress}>
        <View style={styles.link}>
          <Text style={styles.text}>Source code on Github</Text>
        </View>
      </TouchableOpacity>
    </View>
  );
};
