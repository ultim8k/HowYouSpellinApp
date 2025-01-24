import * as React from 'react';
import {createNativeStackNavigator} from '@react-navigation/native-stack';
import 'react-native-gesture-handler';

import {AboutScreen} from './About';
import {HomeScreen} from './Home';
import {Favourites} from './Favourites';

import {colors} from '../constants/colors';
import {HeaderPopupMenu} from '../components/HeaderPopupMenu';
import {SettingsScreen} from './Settings';
import {useTheme} from '../hooks/useTheme';

const RootStack = createNativeStackNavigator();

export const RootNavigator: React.FC = () => {
  const {isDark} = useTheme();

  const headerColor = isDark ? colors.black : colors.white;
  const headerTintColor = isDark ? colors.white : colors.black;

  return (
    <RootStack.Navigator initialRouteName="Home">
      <RootStack.Screen
        name="Home"
        component={HomeScreen}
        options={{
          title: 'How you spellin?',
          headerTintColor: headerTintColor,
          headerRight: HeaderPopupMenu,
          headerStyle: {
            backgroundColor: headerColor,
          },
        }}
      />
      <RootStack.Screen
        name="About Modal"
        component={AboutScreen}
        options={() => ({
          presentation: 'modal',
          title: 'About',
          headerStyle: {
            backgroundColor: colors.orange,
          },
        })}
      />
      <RootStack.Screen
        name="Favourites Modal"
        component={Favourites}
        options={() => ({
          presentation: 'modal',
          title: 'Favourites',
          headerTintColor: headerTintColor,
          headerStyle: {
            backgroundColor: headerColor,
          },
        })}
      />
      <RootStack.Screen
        name="Settings Modal"
        component={SettingsScreen}
        options={() => ({
          presentation: 'modal',
          title: 'Settings',
          headerTintColor: headerTintColor,
          headerStyle: {
            backgroundColor: headerColor,
          },
        })}
      />
    </RootStack.Navigator>
  );
};
