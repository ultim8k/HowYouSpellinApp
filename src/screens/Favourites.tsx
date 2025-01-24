import React from 'react';
import {Alert, StyleSheet, Text, TouchableOpacity, View} from 'react-native';

import {fontSizes} from '../constants/fontSizes';
import {colors} from '../constants/colors';

import {
  getFavouritesWithContent,
  deleteAllFavourites,
  deleteFavouriteByKey,
} from '../utils';
import {FavouritesList} from '../components/FavouritesList';
import {SettingSwitch} from '../components/SettingSwitch';
import {useTheme} from '../hooks/useTheme';
import {FavouriteWithContent} from '../types';

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
  headerWrapper: {
    padding: 20,
  },
  deleteAllButtonWrapper: {
    minHeight: 30,
  },
});

const handleClearAllWithCaution = async (): Promise<void> => {
  return Alert.alert(
    'Delete all items',
    'Are you sure you want to delete all favourites? This cannot be reversed.',
    [
      {
        text: 'Cancel',
        style: 'cancel',
      },
      {
        text: 'Delete',
        onPress: () => {
          deleteAllFavourites(true);
        },
      },
    ],
  );
};

const handleDeleteItemWithCaution = async (title: string): Promise<void> => {
  return Alert.alert(
    'Delete item',
    `Are you sure you want to delete "${title}"? This cannot be reversed.`,
    [
      {
        text: 'Cancel',
        style: 'cancel',
      },
      {
        text: 'Delete',
        onPress: () => {
          deleteFavouriteByKey(title);
        },
      },
    ],
  );
};

interface FavouritesProps {
  navigation: any;
}

export const Favourites = ({navigation}: FavouritesProps) => {
  const [isEditMode, setIsEditMode] = React.useState<boolean>(false);
  const [favourites, setFavourites] = React.useState<FavouriteWithContent[]>(
    [],
  );
  const {styles: themeStyles} = useTheme();

  const fetchFavourites = async () => {
    const items = await getFavouritesWithContent();
    console.log('items', items);
    setFavourites(items);
  };

  React.useEffect(() => {
    fetchFavourites();
  }, []);

  const handleItemInsert = (): void => {
    navigation?.popToTop();
  };

  const handleToggleEditMode = (): void => {
    setIsEditMode(!isEditMode);
  };

  const handleClearAllPress = async (): Promise<void> => {
    await handleClearAllWithCaution();
    fetchFavourites();
  };
  const handleDeleteItemPress = async (title: string): Promise<void> => {
    await handleDeleteItemWithCaution(title);
    fetchFavourites();
  };

  return (
    <View style={[styles.container, themeStyles.backgroundPrimary]}>
      <View style={styles.headerWrapper}>
        <SettingSwitch
          description="Enable edit mode"
          isSettingEnabled={isEditMode}
          toggleSetting={handleToggleEditMode}
        />

        <View style={styles.deleteAllButtonWrapper}>
          {isEditMode && favourites.length > 0 && (
            <TouchableOpacity onPress={handleClearAllPress}>
              <View style={styles.button}>
                <Text style={styles.actionText}>Delete all favorites</Text>
              </View>
            </TouchableOpacity>
          )}
        </View>
      </View>
      <FavouritesList
        favourites={favourites}
        onInsertCallback={handleItemInsert}
        onDeleteCallback={handleDeleteItemPress}
        isEditMode={isEditMode}
      />
    </View>
  );
};
