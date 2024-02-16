import * as React from 'react';
import {StyleSheet, Text, View, TouchableOpacity} from 'react-native';
import {fontSizes} from '../../constants/fontSizes';
import {colors} from '../../constants/colors';
import {useTheme} from '../../hooks/useTheme';

const styles = StyleSheet.create({
  text: {
    textAlign: 'center',
    fontSize: fontSizes.medium,
  },
  pill: {
    borderRadius: 10,
    // boxShadow: '0 0 5px rgba(0, 0, 0, 0.8)',
  },
  pillContentContainer: {
    paddingHorizontal: 12,
    paddingVertical: 5,
    display: 'flex',
    flexDirection: 'row',
    gap: 5,
  },
  pillContentContainerWithIcon: {
    paddingLeft: 12,
    paddingRight: 5,
  },
  icon: {
    color: colors.black,
  },
});

interface FavouritesItemPillProps {
  id: string;
  label: string;
  handlePress: (id: string) => void;
  handleDelete?: (id: string) => void;
  isEditMode?: boolean;
}

export const FavouritesItemPill: React.FC<FavouritesItemPillProps> = ({
  id,
  label,
  handlePress,
  handleDelete,
  isEditMode,
}) => {
  const {styles: themeStyles} = useTheme();

  if (isEditMode && handleDelete) {
    return (
      <View style={[styles.pill, themeStyles.backgroundTertiary]}>
        <View
          style={[
            styles.pillContentContainer,
            styles.pillContentContainerWithIcon,
          ]}>
          <Text style={[styles.text, themeStyles.textPrimary]}>{label}</Text>
          <TouchableOpacity onPress={() => handleDelete(id)}>
            <Text style={[styles.text, styles.icon]}>&times;</Text>
          </TouchableOpacity>
        </View>
      </View>
    );
  }

  return (
    <TouchableOpacity onPress={() => handlePress(id)}>
      <View style={[styles.pill, themeStyles.backgroundTertiary]}>
        <View style={styles.pillContentContainer}>
          <Text style={[styles.text, themeStyles.textPrimary]}>{label}</Text>
        </View>
      </View>
    </TouchableOpacity>
  );
};
