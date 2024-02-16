import {useNavigation} from '@react-navigation/native';
import * as React from 'react';
import {StyleSheet, Text, View, Pressable} from 'react-native';
import {fontSizes} from '../constants/fontSizes';
import {useTheme} from '../hooks/useTheme';

const styles = StyleSheet.create({
  wrapperRow: {
    paddingHorizontal: 10,
    flexDirection: 'row',
    justifyContent: 'flex-start',
  },
  button: {
    paddingVertical: 7,
    paddingHorizontal: 10,
  },
  text: {
    fontSize: fontSizes.small,
  },
});

export const VisitFavouritesButton: React.FC = () => {
  const navigation = useNavigation();
  const {styles: themeStyles} = useTheme();

  return (
    <View style={styles.wrapperRow}>
      <Pressable
        style={styles.button}
        /* @ts-ignore */
        onPress={() => navigation.navigate('FavouritesModal')}>
        <Text style={[styles.text, themeStyles.textSecondary]}>Favourites</Text>
      </Pressable>
    </View>
  );
};
