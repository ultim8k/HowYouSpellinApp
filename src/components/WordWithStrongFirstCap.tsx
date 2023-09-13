import * as React from 'react';
import {Appearance, StyleSheet, Text} from 'react-native';
import {useListOrientation} from '../hooks/useListOrientation';
import {colors} from '../constants/colors';

const styles = StyleSheet.create({
  textWrapperVertical: {
    width: 70,
  },
  strong: {
    fontWeight: '600',
  },
  textDark: {
    color: colors.light,
  },
  textLight: {
    color: colors.black,
  },
});

interface WordWithStrongFirstCapProps {
  text: string;
}

export const WordWithStrongFirstCap: React.FC<WordWithStrongFirstCapProps> = ({
  text,
}) => {
  const [first, ...rest] = text.trim()?.split('');
  const {isHorizontal} = useListOrientation();
  const textStyle = !isHorizontal && styles.textWrapperVertical;
  const colorScheme = Appearance.getColorScheme();
  const textColorStyle =
    colorScheme === 'dark' ? styles.textDark : styles.textLight;

  return (
    <Text style={[textStyle, textColorStyle]}>
      <Text style={styles.strong}>{first}</Text>
      <Text>{rest}</Text>
    </Text>
  );
};
