import * as React from 'react';
import {
  Appearance,
  TouchableOpacity,
  StyleSheet,
  Text,
  View,
  Linking,
} from 'react-native';
import {personalWebsiteLink} from '../constants/links';
import {colors} from '../constants/colors';
import {fontSizes} from '../constants/fontSizes';

const styles = StyleSheet.create({
  madeBy: {
    paddingVertical: 10,
    paddingHorizontal: 20,
    flexDirection: 'row',
    justifyContent: 'center',
    borderBottomWidth: 1,
    borderBottomColor: colors.gray,
    marginBottom: 20,
    marginHorizontal: 20,
  },
  text: {
    fontSize: fontSizes.medium,
    color: colors.darkGray,
  },
  link: {
    color: colors.orange,
  },
  heart: {
    color: colors.red,
  },
  textDark: {
    color: colors.light,
  },
  textLight: {
    color: colors.black,
  },
});

const handleLinkPress = (): void => {
  Linking.openURL(personalWebsiteLink);
};

export const MadeBy: React.FC = () => {
  const colorScheme = Appearance.getColorScheme();
  const textColorStyle =
    colorScheme === 'dark' ? styles.textDark : styles.textLight;

  const heartChar = String.fromCharCode(9829);

  return (
    <View style={styles.madeBy}>
      <Text style={[styles.text, textColorStyle]}>Made with </Text>
      <Text style={StyleSheet.flatten([styles.text, styles.heart])}>
        {heartChar}
      </Text>
      <Text style={[styles.text, textColorStyle]}> by </Text>
      <TouchableOpacity onPress={handleLinkPress}>
        <Text style={StyleSheet.flatten([styles.text, styles.link])}>
          Kostas
        </Text>
      </TouchableOpacity>
    </View>
  );
};
