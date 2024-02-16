import React from 'react';
import {Linking, StyleSheet, Text, View} from 'react-native';
import {MadeBy} from '../components/MadeBy';
import {
  webappLink,
  legalLink,
  githubLink,
  appStoreLink,
} from '../constants/links';
import {colors} from '../constants/colors';
import {fontSizes} from '../constants/fontSizes';
import {useTheme} from '../hooks/useTheme';

const styles = StyleSheet.create({
  text: {
    fontSize: fontSizes.medium,
    marginBottom: 14,
  },
  linkText: {
    color: colors.purple,
  },
  contentContainer: {
    padding: 15,
    flex: 1,
  },
});

const handleAppStoreLinkPress = (): void => {
  Linking.openURL(appStoreLink);
};

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
  const {styles: themeStyles} = useTheme();

  return (
    <View style={[styles.contentContainer, themeStyles.backgroundPrimary]}>
      <MadeBy />
      <Text style={[styles.text, themeStyles.textPrimary]}>
        Thanks for using my app. If you find it useful please consider leaving a
        review on{' '}
        <Text style={styles.linkText} onPress={handleAppStoreLinkPress}>
          the app store
        </Text>
        .
      </Text>
      <Text style={[styles.text, themeStyles.textPrimary]}>
        The app is made with React Native. If you are a developer, you might
        want check the{' '}
        <Text style={styles.linkText} onPress={handleGithubLinkPress}>
          source code on Github
        </Text>
        .
      </Text>
      <Text style={[styles.text, themeStyles.textPrimary]}>
        The app is also available as a web app. You can access it by visiting{' '}
        <Text style={styles.linkText} onPress={handleWebappLinkPress}>
          {webappLink}
        </Text>{' '}
        from any browser.
      </Text>
      <Text style={[styles.text, themeStyles.textPrimary]}>
        As an engineer I am very sensitive about privacy. I do not collect any
        data from the app. You can read the app's{' '}
        <Text style={styles.linkText} onPress={handleLegalLinkPress}>
          privacy policy
        </Text>{' '}
        on the{' '}
        <Text style={styles.linkText} onPress={handleWebappLinkPress}>
          website
        </Text>{' '}
        and feel free to check{' '}
        <Text style={styles.linkText} onPress={handleGithubLinkPress}>
          the source code
        </Text>{' '}
        to make sure.
      </Text>
    </View>
  );
};
