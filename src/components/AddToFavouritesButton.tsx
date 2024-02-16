import React from 'react';
import {Alert, Pressable, StyleSheet, Text} from 'react-native';

import {colors} from '../constants/colors';
import {addFavourite} from '../utils';
import {fontSizes} from '../constants/fontSizes';
import {deleteFavouriteByKey, isTextInFavourites} from '../utils/favourites';

const FILLED_STAR = '★';
const EMPTY_STAR = '☆';

const styles = StyleSheet.create({
  favouriteIconStyle: {
    color: colors.yellowGold,
    fontSize: fontSizes.large,
  },
  favouriteIconStyleDisabled: {
    color: colors.gray,
  },
});

interface AddToFavouritesButtonProps {
  text: string;
}

const handleDeleteFavouritePress = ({
  key,
  callback,
}: {
  key: string;
  callback?: () => void;
}): void => {
  Alert.alert(
    'Remove item from favourites',
    'Are you sure you want to remove this item from favourites?',
    [
      {
        text: 'Cancel',
        style: 'cancel',
      },
      {
        text: 'Remove',
        onPress: () => {
          deleteFavouriteByKey(key);
          callback && callback();
        },
      },
    ],
  );
};

const handleAddFavouritePress = ({
  text,
  callback,
}: {
  text: string;
  callback?: () => void;
}): void => {
  Alert.prompt(
    'Add item to favourites',
    'Optionally specify a different name for this item:',
    [
      {
        text: 'Cancel',
        style: 'cancel',
      },
      {
        text: 'Add',
        onPress: (name?: string) => {
          addFavourite({name, text});
          callback && callback();
        },
      },
    ],
    'plain-text',
    text,
  );
};

export const AddToFavouritesButton: React.FC<AddToFavouritesButtonProps> = ({
  text,
}) => {
  const [isFavourite, setIsFavourite] = React.useState<boolean>(false);
  const handleToggleFavouritePress = (): void => {
    if (!text) {
      return;
    }

    if (isFavourite) {
      handleDeleteFavouritePress({
        key: text,
        callback: () => setIsFavourite(false),
      });
      return;
    }

    handleAddFavouritePress({text, callback: () => setIsFavourite(true)});
  };

  React.useEffect(() => {
    isTextInFavourites(text).then(setIsFavourite);
  }, [text, setIsFavourite]);

  return (
    <Pressable
      disabled={!text}
      onPress={handleToggleFavouritePress}
      style={({pressed}) => [
        {
          opacity: pressed ? 0.5 : 1,
        },
      ]}>
      <Text
        style={[
          styles.favouriteIconStyle,
          !text && styles.favouriteIconStyleDisabled,
        ]}>
        {isFavourite ? FILLED_STAR : EMPTY_STAR}
      </Text>
    </Pressable>
  );
};
