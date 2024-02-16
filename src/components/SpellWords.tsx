import * as React from 'react';
import {StyleSheet, Text, View} from 'react-native';

import {WordWithStrongFirstCap} from './WordWithStrongFirstCap';
import {isBreak} from '../utils';
import {useListOrientation} from '../hooks/useListOrientation';
import {
  KeyboardAwareFlatList,
  KeyboardAwareScrollView,
} from 'react-native-keyboard-aware-scroll-view';
import {colors} from '../constants/colors';
import {useZebraList} from '../hooks/useZebraList';

const styles = StyleSheet.create({
  container: {
    flex: 1,
    maxWidth: '100%',
    justifyContent: 'center',
    alignItems: 'center',
    borderRadius: 3,
  },
  listVertical: {
    textAlign: 'center',
    width: '100%',
    marginVertical: 0,
    marginHorizontal: 'auto',
  },
  scrollViewContentContainer: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    justifyContent: 'center',
  },
  item: {
    padding: 10,
    height: 38,
    letterSpacing: 0.6,
  },
  itemZebraOdd: {
    backgroundColor: colors.lighterGray,
  },
  itemZebraOddText: {
    color: colors.darkGray,
  },
  itemZebraEven: {
    backgroundColor: colors.darkGray,
  },
  itemZebraEvenText: {
    color: colors.lighterGray,
  },
  itemVertical: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  breakItem: {
    flexBasis: '100%',
    textAlign: 'center',
  },
  breakText: {
    color: colors.gray,
    textAlign: 'center',
  },
});

interface ItemProps {
  word: string;
  index: number;
}

const Item: React.FC<ItemProps> = ({word, index}) => {
  const {isHorizontal} = useListOrientation();
  const textItemStyle = !isHorizontal && styles.itemVertical;
  const {isZebraListEnabled} = useZebraList();

  const zebraBoxStyle = isZebraListEnabled
    ? index % 2 === 0
      ? styles.itemZebraOdd
      : styles.itemZebraEven
    : {};

  const zebraTextStyle = isZebraListEnabled
    ? index % 2 === 0
      ? styles.itemZebraOddText
      : styles.itemZebraEvenText
    : {};

  if (isBreak(word)) {
    return (
      <View style={StyleSheet.flatten([styles.item, styles.breakItem])}>
        <Text style={styles.breakText}>{word}</Text>
      </View>
    );
  }

  return (
    <View
      style={StyleSheet.flatten([styles.item, textItemStyle, zebraBoxStyle])}>
      <WordWithStrongFirstCap text={word} style={zebraTextStyle} />
    </View>
  );
};

const renderVerticalListItem = ({
  item,
  index,
}: {
  item: string;
  index: number;
}) => <Item word={item} index={index} />;
const renderHorizontalListItem = (word: string, index: number) => (
  <Item word={word} index={index} key={`${index} ${word}`} />
);

interface SpellWordsProps {
  words: string[];
}

export const SpellWords = ({words}: SpellWordsProps) => {
  const {isHorizontal} = useListOrientation();

  if (isHorizontal) {
    return (
      <View style={styles.container}>
        <KeyboardAwareScrollView
          contentContainerStyle={styles.scrollViewContentContainer}>
          {words.map(renderHorizontalListItem)}
        </KeyboardAwareScrollView>
      </View>
    );
  }

  return (
    <View style={styles.container}>
      <KeyboardAwareFlatList
        style={styles.listVertical}
        data={words}
        renderItem={renderVerticalListItem}
        keyExtractor={(word, index) => `${index} ${word}`}
        horizontal={false}
      />
    </View>
  );
};
