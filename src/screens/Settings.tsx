import React from 'react';
import {StyleSheet, Text, View} from 'react-native';
import {FullWidthItemContainer} from '../components/FullWidthItemContainer';
import {useTheme} from '../hooks/useTheme';
import {SettingSwitch} from '../components/SettingSwitch';
import {useZebraList} from '../hooks/useZebraList';
import {useListOrientation} from '../hooks/useListOrientation';

const styles = StyleSheet.create({
  text: {
    fontWeight: 'bold',
  },
  contentContainer: {
    flex: 1,
    padding: 15,
  },
  listCategoryTitleContainer: {
    marginBottom: 15,
  },
});

export const Settings = () => {
  const {
    isDark,
    styles: themeStyles,
    toggleScheme,
    doesUseSystemSetting,
    toggleDoesUseSystemSetting,
  } = useTheme();
  const {isZebraListEnabled, toggleZebraListEnabled} = useZebraList();
  const {isHorizontal, changeListOrientation} = useListOrientation();

  return (
    <View style={[styles.contentContainer, themeStyles.backgroundPrimary]}>
      <View style={styles.listCategoryTitleContainer}>
        <Text style={[styles.text, themeStyles.textPrimary]}>List options</Text>
      </View>
      <FullWidthItemContainer>
        <SettingSwitch
          description="Display words horizontally"
          isSettingEnabled={isHorizontal}
          toggleSetting={changeListOrientation}
        />
      </FullWidthItemContainer>
      <FullWidthItemContainer>
        <SettingSwitch
          description="Display zebra colors (experimental)"
          isSettingEnabled={isZebraListEnabled}
          toggleSetting={toggleZebraListEnabled}
        />
      </FullWidthItemContainer>
      <View style={styles.listCategoryTitleContainer}>
        <Text style={[styles.text, themeStyles.textPrimary]}>
          Color scheme options
        </Text>
      </View>
      <FullWidthItemContainer>
        <SettingSwitch
          description="Use system colors"
          isSettingEnabled={doesUseSystemSetting}
          toggleSetting={toggleDoesUseSystemSetting}
        />
        <SettingSwitch
          description="Dark mode"
          isSettingEnabled={isDark}
          toggleSetting={toggleScheme}
          switchDisabled={doesUseSystemSetting}
        />
      </FullWidthItemContainer>
      <View style={styles.listCategoryTitleContainer}>
        <Text style={[styles.text, themeStyles.textPrimary]}>
          Text input options
        </Text>
      </View>
      <FullWidthItemContainer>
        <SettingSwitch
          description="Move text input to bottom (coming soon)"
          isSettingEnabled={false}
          toggleSetting={() => {}}
          switchDisabled={true}
        />
      </FullWidthItemContainer>
      <View style={styles.listCategoryTitleContainer}>
        <Text style={[styles.text, themeStyles.textPrimary]}>
          Favourites options
        </Text>
      </View>
      <View>
        <Text>Delete all favourites</Text>
        <Text>Button</Text>

        {/* <FavouritesList onInsertCallback={() => {}} /> */}
      </View>
    </View>
  );
};
