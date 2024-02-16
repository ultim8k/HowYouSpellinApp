import {useNavigation} from '@react-navigation/native';
import * as React from 'react';
import {StyleSheet, Text, View, Pressable} from 'react-native';
import {fontSizes} from '../constants/fontSizes';
import {useTheme} from '../hooks/useTheme';

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
  },
});

export const AboutButton: React.FC = () => {
  const navigation = useNavigation();
  const {styles: themeStyles} = useTheme();

  return (
    <View style={styles.wrapperRow}>
      <Pressable
        style={styles.button}
        /* @ts-ignore */
        onPress={() => navigation.navigate('AboutModal')}>
        <Text style={[styles.text, themeStyles.textSecondary]}>About</Text>
      </Pressable>
    </View>
  );
};
