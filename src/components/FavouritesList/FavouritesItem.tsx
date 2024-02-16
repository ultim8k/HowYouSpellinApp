import React from 'react';
import {Pressable, StyleSheet, Text, View} from 'react-native';

import {FullWidthItemContainer} from '../FullWidthItemContainer';
import {useTheme} from '../../hooks/useTheme';

const itemStyles = StyleSheet.create({
  itemContainer: {
    paddingHorizontal: 20,
  },
  title: {
    fontWeight: 'bold',
    // marginBottom: 5,
  },
});

interface FavouritesItemProps {
  title: string;
  text: string;
  onInsertPress: () => void;
  onDeletePress?: () => void;
}

export const FavouritesItem = ({
  title,
  text,
  onInsertPress,
}: FavouritesItemProps) => {
  const {styles: themeStyles} = useTheme();

  return (
    <View style={itemStyles.itemContainer}>
      <FullWidthItemContainer>
        <Text style={[itemStyles.title, themeStyles.textPrimary]}>{title}</Text>
        <Text style={[themeStyles.textPrimary]}>{text}</Text>
        <Pressable onPress={onInsertPress}>
          <Text style={[themeStyles.textPrimary]}>⌲</Text>
        </Pressable>
      </FullWidthItemContainer>
    </View>
  );
};
