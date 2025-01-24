import React from 'react';
import {Pressable, StyleSheet, Text, View} from 'react-native';

import {FullWidthItemContainer} from '../FullWidthItemContainer';
import {useTheme} from '../../hooks/useTheme';
import {colors} from '../../constants/colors';
import {fontSizes} from '../../constants/fontSizes';

const itemStyles = StyleSheet.create({
  actionText: {
    fontSize: fontSizes.medium,
  },
  itemContainer: {
    paddingHorizontal: 20,
  },
  title: {
    fontWeight: 'bold',
  },
  icon: {
    fontSize: fontSizes.medium,
    fontWeight: 'bold',
  },
  danger: {
    color: colors.red,
  },
  headerWrapper: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    maxWidth: '100%',
    gap: 10,
  },
  titleWrapper: {
    flexGrow: 0,
    flexShrink: 1,
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
          <View style={itemStyles.titleWrapper}>
            <Text style={[itemStyles.title, themeStyles.textPrimary]}>
              {title}
            </Text>
          </View>

          {isEditMode ? (
            <Pressable onPress={onDeletePress}>
              <Text style={[itemStyles.actionText, itemStyles.danger]}>
                Delete item{' '}
                <Text style={[itemStyles.icon, itemStyles.danger]}>×</Text>
              </Text>
            </Pressable>
          ) : (
            <Pressable onPress={onInsertPress}>
              <Text style={[itemStyles.actionText, themeStyles.textSecondary]}>
                Insert item <Text style={[itemStyles.icon]}>〉</Text>
              </Text>
            </Pressable>
          )}
        </View>
        <Text style={[themeStyles.textPrimary]}>{text}</Text>
      </FullWidthItemContainer>
    </View>
  );
};
