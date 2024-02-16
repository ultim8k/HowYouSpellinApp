import * as React from 'react';

type InputBarPosition = 'top' | 'bottom';
const InputBarPositionContext = React.createContext<{
  toggleInputBarPosition: () => void;
  isBottom: boolean;
}>({
  toggleInputBarPosition: () => {},
  isBottom: false,
});

export const useInputBarPosition = () =>
  React.useContext(InputBarPositionContext);

interface InputBarPositionProviderProps {
  children: React.JSX.Element;
}

export const InputBarPositionProvider: React.FC<
  InputBarPositionProviderProps
> = ({children}) => {
  const [inputBarPosition, setInputBarPosition] =
    React.useState<InputBarPosition>('top');

  const toggleInputBarPosition = (): void => {
    const nextPosition: InputBarPosition =
      inputBarPosition === 'top' ? 'bottom' : 'top';
    setInputBarPosition(nextPosition);
  };

  const isBottom = inputBarPosition === 'bottom';

  console.log('isBottom', isBottom);

  return (
    <InputBarPositionContext.Provider
      value={{
        toggleInputBarPosition,
        isBottom,
      }}>
      {children}
    </InputBarPositionContext.Provider>
  );
};
