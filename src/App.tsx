import React from 'react';

import {NavigationContainer} from '@react-navigation/native';
import {ThemeProvider} from './hooks/useTheme';
import {ListOrientationProvider} from './hooks/useListOrientation';
import {RootNavigator} from './screens/Root.Navigator';
import {InputBarPositionProvider} from './hooks/useInputBarPosition';
import {InputTextProvider} from './hooks/useInputText';
import {ZebraListProvider} from './hooks/useZebraList';

const App = (): React.JSX.Element => (
  <NavigationContainer>
    <ThemeProvider>
      <ListOrientationProvider>
        <ZebraListProvider>
          <InputBarPositionProvider>
            <InputTextProvider>
              <RootNavigator />
            </InputTextProvider>
          </InputBarPositionProvider>
        </ZebraListProvider>
      </ListOrientationProvider>
    </ThemeProvider>
  </NavigationContainer>
);

export default App;
