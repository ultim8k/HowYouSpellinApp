import * as React from 'react';
import {Platform, Settings} from 'react-native';
import {settingsNames} from '../constants/settingsNames';

type ListOrientation = 'horizontal' | 'vertical';
const ListOrientationContext = React.createContext<{
  changeListOrientation: () => void;
  isHorizontal: boolean;
}>({
  changeListOrientation: () => {},
  isHorizontal: false,
});

export const useListOrientation = () =>
  React.useContext(ListOrientationContext);

interface ListOrientationProviderProps {
  children: React.JSX.Element;
}

export const ListOrientationProvider: React.FC<
  ListOrientationProviderProps
> = ({children}) => {
  const [listOrientation, setListOrientation] = React.useState<ListOrientation>(
    () =>
      Platform.OS === 'ios'
        ? Settings.get(settingsNames.listOrientation)
        : 'horizontal',
  );

  if (Platform.OS === 'ios') {
    Settings.watchKeys([settingsNames.listOrientation], () => {
      setListOrientation(
        Settings.get(settingsNames.listOrientation) || 'horizontal',
      );
    });
  }

  const defaultContext = React.useMemo(() => {
    const isHorizontal = listOrientation === 'horizontal';
    const changeListOrientation = (): void => {
      const nextOrientation: ListOrientation =
        listOrientation === 'horizontal' ? 'vertical' : 'horizontal';
      if (Platform.OS === 'ios') {
        Settings.set({[settingsNames.listOrientation]: nextOrientation});
      }
      setListOrientation(nextOrientation);
    };

    return {changeListOrientation, isHorizontal};
  }, [listOrientation]);

  return (
    <ListOrientationContext.Provider value={defaultContext}>
      {children}
    </ListOrientationContext.Provider>
  );
};
