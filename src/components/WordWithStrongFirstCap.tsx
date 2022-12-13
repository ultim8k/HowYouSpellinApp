import * as React from 'react';
import {StyleSheet, Text} from 'react-native';
import {useListOrientation} from '../hooks/useListOrientation';

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
}

export const WordWithStrongFirstCap: React.FC<WordWithStrongFirstCapProps> = ({
  text,
}) => {
  const [first, ...rest] = text.trim()?.split('');
  const {isHorizontal} = useListOrientation();
  const textStyle = !isHorizontal && styles.textWrapperVertical;

  return (
    <Text style={textStyle}>
      <Text style={styles.strong}>{first}</Text>
      <Text>{rest}</Text>
    </Text>
  );
};
