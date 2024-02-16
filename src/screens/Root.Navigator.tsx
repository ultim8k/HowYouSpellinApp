import * as React from 'react';
import {createNativeStackNavigator} from '@react-navigation/native-stack';
import 'react-native-gesture-handler';

import {About} from './About';
import {Home} from './Home';
import {Favourites} from './Favourites';

import {colors} from '../constants/colors';
// import {VisitFavouritesButton} from '../components/VisitFavouritesButton';
// import {VisitAboutButton} from '../components/VisitAboutButton';
import {HeaderPopupMenu} from '../components/HeaderPopupMenu';
import {Settings} from './Settings';
import {useTheme} from '../hooks/useTheme';
// import {fontSizes} from '../constants/fontSizes';

const RootStack = createNativeStackNavigator();

export const RootNavigator: React.FC = () => {
  const {isDark} = useTheme();

  const headerColor = isDark ? colors.black : colors.white;
  const headerTintColor = isDark ? colors.white : colors.black;

  return (
    <RootStack.Navigator initialRouteName="Home">
      <RootStack.Screen
        name="Home"
        component={Home}
        options={{
          // headerTitle: '',
          title: 'How you spellin?',
          headerTintColor: headerTintColor,
          headerRight: HeaderPopupMenu,
          headerStyle: {
            backgroundColor: headerColor,
          },
        }}
      />
      <RootStack.Screen
        name="AboutModal"
        component={About}
        options={() => ({
          presentation: 'modal',
          title: 'About',
          headerStyle: {
            backgroundColor: colors.orange,
          },
        })}
      />
      <RootStack.Screen
        name="FavouritesModal"
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
        name="SettingsModal"
        component={Settings}
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
