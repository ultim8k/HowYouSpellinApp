import React from 'react';
import {
  Alert,
  AlertButton,
  Platform,
  Pressable,
  StyleSheet,
  Text,
} from 'react-native';

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
  const promptTitle = 'Add item to favourites';
  const promptMessage = 'Optionally specify a different name for this item:';
  const cancelButtonOptions = {
    text: 'Cancel',
    style: 'cancel',
  } as AlertButton;
  const addButtonOptions = {
    text: 'Add',
    onPress: (name?: string) => {
      addFavourite({name, text});
      callback && callback();
    },
  } as AlertButton;

  if (Platform.OS === 'ios') {
    Alert.prompt(
      promptTitle,
      promptMessage,
      [cancelButtonOptions, addButtonOptions],
      'plain-text',
      text,
    );
  } else {
    Alert.alert(promptTitle, `"${text}"`, [
      cancelButtonOptions,
      addButtonOptions,
    ]);
  }
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
