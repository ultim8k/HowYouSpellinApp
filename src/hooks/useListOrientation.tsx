import * as React from 'react';

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
  const [listOrientation, setListOrientation] =
    React.useState<ListOrientation>('horizontal');

  const defaultContext = React.useMemo(() => {
    const isHorizontal = listOrientation === 'horizontal';
    const changeListOrientation = (): void => {
      const nextOrientation: ListOrientation =
        listOrientation === 'horizontal' ? 'vertical' : 'horizontal';
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
