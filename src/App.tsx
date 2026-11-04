import React from 'react';
import {NavigationContainer} from '@react-navigation/native';
import {SafeAreaProvider} from 'react-native-safe-area-context';
import {ListOrientationProvider} from './hooks/useListOrientation';
import {RootNavigator} from './screens/Root.Navigator';

const App = (): React.JSX.Element => (
  <SafeAreaProvider>
    <NavigationContainer>
      <ListOrientationProvider>
        <RootNavigator />
      </ListOrientationProvider>
    </NavigationContainer>
  </SafeAreaProvider>
);

export default App;
