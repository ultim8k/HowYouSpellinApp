import React from 'react';
import {StyleSheet, View, FlatList} from 'react-native';

import {useInputText} from '../../hooks/useInputText';
import {fontSizes} from '../../constants/fontSizes';
import {colors} from '../../constants/colors';

import {FavouritesItem} from './FavouritesItem';
import {FavouriteWithContent} from '../../types';

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
    flex: 1,
    textAlign: 'center',
    width: '100%',
    marginVertical: 0,
    marginHorizontal: 'auto',
    gap: 10,
    justifyContent: 'center',
  },
});

interface FavouritesListProps {
  favourites: FavouriteWithContent[];
  onInsertCallback: (text?: string) => void;
  onDeleteCallback: (title: string) => void;
  isEditMode?: boolean;
}

export const FavouritesList = ({
  favourites,
  onInsertCallback,
  onDeleteCallback,
  isEditMode,
}: FavouritesListProps) => {
  const {updateInputText} = useInputText();

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
            onDeletePress={() => onDeleteCallback(item.title)}
            isEditMode={isEditMode}
          />
        )}
        horizontal={false}
      />
    </View>
  );
};
