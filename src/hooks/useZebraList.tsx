import * as React from 'react';
import {Platform, Settings} from 'react-native';
import {settingsNames} from '../constants/settingsNames';

const ZebraListContext = React.createContext<{
  toggleZebraListEnabled: () => void;
  isZebraListEnabled: boolean;
}>({
  toggleZebraListEnabled: () => {},
  isZebraListEnabled: false,
});

export const useZebraList = () => React.useContext(ZebraListContext);

interface ZebraListProviderProps {
  children: React.JSX.Element;
}

export const ZebraListProvider: React.FC<ZebraListProviderProps> = ({
  children,
}) => {
  const [isZebraListEnabled, setIsZebraListEnabled] = React.useState<boolean>(
    () =>
      Platform.OS === 'ios'
        ? Settings.get(settingsNames.zebraListToggle) ?? false
        : false,
  );

  if (Platform.OS === 'ios') {
    Settings.watchKeys([settingsNames.zebraListToggle], () => {
      setIsZebraListEnabled(
        Settings.get(settingsNames.zebraListToggle) ?? false,
      );
    });
  }

  const defaultContext = React.useMemo(() => {
    const toggleZebraListEnabled = (): void => {
      if (Platform.OS === 'ios') {
        Settings.set({[settingsNames.zebraListToggle]: !isZebraListEnabled});
      }

      setIsZebraListEnabled(!isZebraListEnabled);
    };

    return {toggleZebraListEnabled, isZebraListEnabled};
  }, [isZebraListEnabled]);

  return (
    <ZebraListContext.Provider value={defaultContext}>
      {children}
    </ZebraListContext.Provider>
  );
};
