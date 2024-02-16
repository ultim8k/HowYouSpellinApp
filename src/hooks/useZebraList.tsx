import * as React from 'react';

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
  const [isZebraListEnabled, setIsZebraListEnabled] =
    React.useState<boolean>(false);

  const defaultContext = React.useMemo(() => {
    const toggleZebraListEnabled = (): void =>
      setIsZebraListEnabled(!isZebraListEnabled);

    return {toggleZebraListEnabled, isZebraListEnabled};
  }, [isZebraListEnabled]);

  return (
    <ZebraListContext.Provider value={defaultContext}>
      {children}
    </ZebraListContext.Provider>
  );
};
