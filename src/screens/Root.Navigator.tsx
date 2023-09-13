import * as React from 'react';
import {createNativeStackNavigator} from '@react-navigation/native-stack';

import {About} from './About';
import {Home} from './Home';
import {colors} from '../constants/colors';

const RootStack = createNativeStackNavigator();

export const RootNavigator: React.FC = () => {
  return (
    <RootStack.Navigator>
      <RootStack.Screen
        name="Home"
        component={Home}
        options={{headerShown: false}}
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
    </RootStack.Navigator>
  );
};
