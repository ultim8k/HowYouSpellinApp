import React from 'react';
import {ListOrientationProvider} from './hooks/useListOrientation';
import Home from './screens/Home';

const App: React.FC = () => (
  <ListOrientationProvider>
    <Home />
  </ListOrientationProvider>
);

export default App;
