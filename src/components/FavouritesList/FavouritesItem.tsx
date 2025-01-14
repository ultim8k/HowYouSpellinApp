import React from 'react';
import {Pressable, StyleSheet, Text, View} from 'react-native';

import {FullWidthItemContainer} from '../FullWidthItemContainer';
import {useTheme} from '../../hooks/useTheme';
import {colors} from '../../constants/colors';

const itemStyles = StyleSheet.create({
  itemContainer: {
    paddingHorizontal: 20,
  },
  title: {
    fontWeight: 'bold',
    // marginBottom: 5,
  },
  icon: {
    fontSize: 20,
    color: colors.purple,
    fontWeight: 'bold',
  },
  danger: {
    color: colors.red,
  },
  headerWrapper: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
});

interface FavouritesItemProps {
  title: string;
  text: string;
  onInsertPress: () => void;
  onDeletePress?: () => void;
  isEditMode?: boolean;
}

export const FavouritesItem = ({
  title,
  text,
  onInsertPress,
  onDeletePress,
  isEditMode,
}: FavouritesItemProps) => {
  const {styles: themeStyles} = useTheme();

  return (
    <View style={itemStyles.itemContainer}>
      <FullWidthItemContainer>
        <View style={itemStyles.headerWrapper}>
          <Text style={[itemStyles.title, themeStyles.textPrimary]}>
            {title}
          </Text>

          {isEditMode ? (
            <Pressable onPress={onDeletePress}>
              <Text style={[itemStyles.icon, itemStyles.danger]}>♻︎</Text>
            </Pressable>
          ) : (
            <Pressable onPress={onInsertPress}>
              <Text style={[itemStyles.icon]}>⎘</Text>
            </Pressable>
          )}
        </View>
        <Text style={[themeStyles.textPrimary]}>{text}</Text>
      </FullWidthItemContainer>
    </View>
  );
};
