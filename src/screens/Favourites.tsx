import React from 'react';
import {Alert, StyleSheet, Text, TouchableOpacity, View} from 'react-native';

import {fontSizes} from '../constants/fontSizes';
import {colors} from '../constants/colors';

import {getFavourites, deleteAllFavourites} from '../utils';
import {FavouritesList} from '../components/FavouritesList';
import {useTheme} from '../hooks/useTheme';

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  actionText: {
    textAlign: 'center',
    fontSize: fontSizes.medium,
    color: colors.purple,
  },
  button: {
    marginTop: 5,
    marginBottom: 5,
  },
});

const handleClearAllPress = (): void => {
  Alert.alert(
    'Clear all items',
    'Are you sure you want to delete all favourites? This cannot be reversed.',
    [
      {
        text: 'Cancel',
        style: 'cancel',
      },
      {
        text: 'Delete',
        onPress: () => deleteAllFavourites(true),
      },
    ],
  );
};

interface FavouritesProps {
  navigation: any;
}

export const Favourites = ({navigation}: FavouritesProps) => {
  const [favourites, setFavourites] = React.useState<string[]>([]);
  const {styles: themeStyles} = useTheme();

  React.useEffect(() => {
    const fetchFavourites = async () => {
      const items = await getFavourites();
      setFavourites(items);
    };
    fetchFavourites();
  }, []);

  const handleItemInsert = (): void => {
    navigation?.popToTop();
  };

  return (
    <View style={[styles.container, themeStyles.backgroundPrimary]}>
      <FavouritesList onInsertCallback={handleItemInsert} />
      {/* {favourites.length > 0 && (
        <TouchableOpacity onPress={handleClearAllPress}>
          <View style={styles.button}>
            <Text style={styles.actionText}>Clear all</Text>
          </View>
        </TouchableOpacity>
      )} */}
    </View>
  );
};
