import {useNavigation} from '@react-navigation/native';
import * as React from 'react';
import {StyleSheet, Text, View, Pressable, Appearance} from 'react-native';
import {colors} from '../constants/colors';
import {fontSizes} from '../constants/fontSizes';

const styles = StyleSheet.create({
  wrapperRow: {
    paddingHorizontal: 10,
    flexDirection: 'row',
    justifyContent: 'flex-end',
  },
  button: {
    paddingVertical: 7,
    paddingHorizontal: 10,
  },
  text: {
    fontSize: fontSizes.small,
    color: colors.darkGray,
  },
  textDark: {
    color: colors.lightGray,
  },
  textLight: {
    color: colors.darkGray,
  },
});

export const AboutButton: React.FC = () => {
  const navigation = useNavigation();
  const colorScheme = Appearance.getColorScheme();
  const textColorStyle =
    colorScheme === 'dark' ? styles.textDark : styles.textLight;

  return (
    <View style={styles.wrapperRow}>
      <Pressable
        style={styles.button}
        /* @ts-ignore */
        onPress={() => navigation.navigate('AboutModal')}>
        <Text style={[styles.text, textColorStyle]}>About</Text>
      </Pressable>
    </View>
  );
};
