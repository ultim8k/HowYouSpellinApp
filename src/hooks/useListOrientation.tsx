import * as React from 'react';

type ListOrientation = 'horizontal' | 'vertical';
const ListOrientationContext = React.createContext<{
  changeListOrientation?: () => void;
  isHorizontal: boolean;
}>({
  changeListOrientation: () => {},
  isHorizontal: false,
});

export const useListOrientation = () =>
  React.useContext(ListOrientationContext);

interface ListOrientationProviderProps {
  children: JSX.Element;
}

export const ListOrientationProvider: React.FC<
  ListOrientationProviderProps
> = ({children}) => {
  const [listOrientation, setListOrientation] =
    React.useState<ListOrientation>('vertical');

  const changeListOrientation = (): void => {
    const nextOrientation: ListOrientation =
      listOrientation === 'horizontal' ? 'vertical' : 'horizontal';
    setListOrientation(nextOrientation);
  };

  const isHorizontal = listOrientation === 'horizontal';

  return (
    <ListOrientationContext.Provider
      value={{
        changeListOrientation,
        isHorizontal,
      }}>
      {children}
    </ListOrientationContext.Provider>
  );
};
