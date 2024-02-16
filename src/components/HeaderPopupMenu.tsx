import React from 'react';
import {useNavigation} from '@react-navigation/native';

import ContextMenu from 'react-native-context-menu-view';
import {DotsButton} from './DotsButton';

export const HeaderPopupMenu: React.FC = () => {
  const navigation = useNavigation();
  return (
    <ContextMenu
      actions={[
        {title: 'About', systemIcon: 'info.circle'},
        {title: 'Favourites', systemIcon: 'star'},
        {title: 'Settings', systemIcon: 'gear'},
      ]}
      onPress={e => {
        const key = e.nativeEvent?.name.toLowerCase();

        if (key === 'about') {
          /* @ts-ignore */
          navigation.navigate('AboutModal');
          return;
        }

        if (key === 'favourites') {
          /* @ts-ignore */
          navigation.navigate('FavouritesModal');
          return;
        }

        if (key === 'settings') {
          /* @ts-ignore */
          navigation.navigate('SettingsModal');
          return;
        }
      }}
      dropdownMenuMode={true}>
      <DotsButton />
    </ContextMenu>
  );
};
