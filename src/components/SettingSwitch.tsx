import React from 'react';
import {StyleSheet, Switch, Text, View} from 'react-native';

import {useTheme} from '../hooks/useTheme';
import {fontSizes} from '../constants/fontSizes';

const styles = StyleSheet.create({
  switchBoxLine: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  textStyle: {
    fontSize: fontSizes.medium,
  },
});

interface SettingSwitchProps {
  description: string;
  isSettingEnabled: boolean;
  toggleSetting: () => void;
  switchDisabled?: boolean;
}

export const SettingSwitch: React.FC<SettingSwitchProps> = ({
  description,
  isSettingEnabled,
  toggleSetting,
  switchDisabled = false,
}) => {
  const {styles: themeStyles} = useTheme();

  return (
    <View style={styles.switchBoxLine}>
      <Text style={[styles.textStyle, themeStyles.textPrimary]}>
        {description}
      </Text>
      <Switch
        onValueChange={toggleSetting}
        value={isSettingEnabled}
        disabled={switchDisabled}
      />
    </View>
  );
};
