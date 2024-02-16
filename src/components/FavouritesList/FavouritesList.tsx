import React from 'react';
import {
  // Alert,
  StyleSheet,
  View,
  FlatList,
} from 'react-native';

import {useInputText} from '../../hooks/useInputText';
import {fontSizes} from '../../constants/fontSizes';
import {colors} from '../../constants/colors';

import {FavouritesItem} from './FavouritesItem';
import {getFavouritesWithContent} from '../../utils/favourites';

const styles = StyleSheet.create({
  text: {
    textAlign: 'center',
    fontSize: fontSizes.medium,
    color: colors.purple,
  },
  button: {
    marginTop: 5,
    marginBottom: 5,
  },
  listContainer: {
    textAlign: 'center',
    width: '100%',
    marginVertical: 0,
    marginHorizontal: 'auto',
    paddingTop: 20,
    paddingBottom: 20,
    gap: 10,
    justifyContent: 'center',
    // marginBottom: 10,
  },
});

interface FavouritesListProps {
  onInsertCallback: (text?: string) => void;
}

export const FavouritesList = ({onInsertCallback}: FavouritesListProps) => {
  const [favourites, setFavourites] = React.useState<
    {
      title: string;
      text: string;
    }[]
  >([]);
  const {updateInputText} = useInputText();

  React.useEffect(() => {
    const fetchFavourites = async () => {
      const items = await getFavouritesWithContent();
      setFavourites(items);
    };
    fetchFavourites();
  }, []);

  const handleItemInsertPress = async (text: string): Promise<void> => {
    updateInputText(text);
    onInsertCallback && onInsertCallback(text);
  };

  return (
    <View style={styles.listContainer}>
      <FlatList
        data={favourites}
        renderItem={({item}) => (
          <FavouritesItem
            title={item.title}
            text={item.text}
            onInsertPress={() => handleItemInsertPress(item.text)}
          />
        )}
        horizontal={false}
      />
    </View>
  );
};
