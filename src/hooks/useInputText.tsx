import * as React from 'react';

const InputTextContext = React.createContext<{
  updateInputText: (text: string) => void;
  text: string;
}>({
  updateInputText: () => {},
  text: '',
});

export const useInputText = () => React.useContext(InputTextContext);

interface InputTextProviderProps {
  children: React.JSX.Element;
}

export const InputTextProvider: React.FC<InputTextProviderProps> = ({
  children,
}) => {
  const [text, setText] = React.useState<string>('');
  const providerValue = React.useMemo(() => {
    const updateInputText = (value: string = ''): void => {
      setText(value);
    };

    return {
      updateInputText,
      text,
    };
  }, [text]);

  return (
    <InputTextContext.Provider value={providerValue}>
      {children}
    </InputTextContext.Provider>
  );
};
