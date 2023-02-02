import React from 'react';
import {NavigationContainer} from '@react-navigation/native';
import {ListOrientationProvider} from './hooks/useListOrientation';
import {RootNavigator} from './screens/Root.Navigator';

const App: React.FC = () => (
  <NavigationContainer>
    <ListOrientationProvider>
      <RootNavigator />
    </ListOrientationProvider>
  </NavigationContainer>
);

export default App;
