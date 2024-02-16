import React from 'react';
import {StyleSheet, View} from 'react-native';

import {FavouritesItemPill} from './FavouritesItemPill';

import {getFavourites, getFavouriteByKey} from '../../utils';
import {useInputText} from '../../hooks/useInputText';

const styles = StyleSheet.create({
  favouritesList: {
    paddingVertical: 20,
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: 10,
    justifyContent: 'center',
  },
});

export const FavouritesPills: React.FC = () => {
  const [favourites, setFavourites] = React.useState<string[]>([]);
  const {updateInputText} = useInputText();

  const fetchFavourites = async () => {
    const items = await getFavourites();
    setFavourites(items);
  };

  React.useEffect(() => {
    fetchFavourites();
  }, []);

  const handleItemPress = async (key: string): Promise<void> => {
    const text = await getFavouriteByKey(key);
    updateInputText(text);
    console.log('Fill item', key, text);
  };

  console.log('favoutites', favourites);

  return (
    <View style={styles.favouritesList}>
      {favourites.map(key => (
        <FavouritesItemPill
          key={key}
          id={key}
          label={key}
          handlePress={handleItemPress}
        />
      ))}
    </View>
  );
};
