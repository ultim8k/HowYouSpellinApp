import * as React from 'react';
import {StyleSheet, Text} from 'react-native';
import {useListOrientation} from '../hooks/useListOrientation';
import {useTheme} from '../hooks/useTheme';

const styles = StyleSheet.create({
  textWrapperVertical: {
    width: 70,
  },
  strong: {
    fontWeight: '600',
  },
});

interface WordWithStrongFirstCapProps {
  text: string;
  style?: any;
}

export const WordWithStrongFirstCap: React.FC<WordWithStrongFirstCapProps> = ({
  text,
  style,
}) => {
  const [first, ...rest] = text.trim()?.split('');
  const {isHorizontal} = useListOrientation();
  const textStyle = !isHorizontal && styles.textWrapperVertical;
  const {styles: themeStyles} = useTheme();

  return (
    <Text style={[textStyle, themeStyles.textPrimary, style]}>
      <Text style={styles.strong}>{first}</Text>
      <Text>{rest}</Text>
    </Text>
  );
};
