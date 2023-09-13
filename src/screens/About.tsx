import React from 'react';
import {
  Appearance,
  Linking,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from 'react-native';
import {MadeBy} from '../components/MadeBy';
import {webappLink, legalLink, githubLink} from '../constants/links';
import {colors} from '../constants/colors';
import {fontSizes} from '../constants/fontSizes';

const styles = StyleSheet.create({
  text: {
    textAlign: 'center',
    fontSize: fontSizes.medium,
    color: colors.purple,
  },
  link: {
    marginBottom: 5,
  },
  container: {
    flex: 1,
  },
  containerDark: {
    backgroundColor: colors.dark,
  },
  containerLight: {
    backgroundColor: colors.light,
  },
});

const handleWebappLinkPress = (): void => {
  Linking.openURL(webappLink);
};

const handleLegalLinkPress = (): void => {
  Linking.openURL(legalLink);
};

const handleGithubLinkPress = (): void => {
  Linking.openURL(githubLink);
};

export const About: React.FC = () => {
  const colorScheme = Appearance.getColorScheme();
  const containerColorStyle =
    colorScheme === 'dark' ? styles.containerDark : styles.containerLight;

  return (
    <View style={[styles.container, containerColorStyle]}>
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
