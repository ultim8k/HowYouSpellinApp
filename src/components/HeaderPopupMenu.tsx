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
          navigation.navigate('About Modal');
          return;
        }

        if (key === 'favourites') {
          /* @ts-ignore */
          navigation.navigate('Favourites Modal');
          return;
        }

        if (key === 'settings') {
          /* @ts-ignore */
          navigation.navigate('Settings Modal');
        }
      }}
      dropdownMenuMode={true}>
      <DotsButton />
    </ContextMenu>
  );
};
