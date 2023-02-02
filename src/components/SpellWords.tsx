import * as React from 'react';
import {StyleSheet, Text, View} from 'react-native';

import {WordWithStrongFirstCap} from './WordWithStrongFirstCap';
import {isBreak} from '../utils';
import {useListOrientation} from '../hooks/useListOrientation';
import {
  KeyboardAwareFlatList,
  KeyboardAwareScrollView,
} from 'react-native-keyboard-aware-scroll-view';

const styles = StyleSheet.create({
  container: {
    flex: 1,
    maxWidth: '100%',
    justifyContent: 'center',
    alignItems: 'center',
  },
  listVertical: {
    textAlign: 'center',
    listStyle: 'none',
    width: '100%',
    marginVertical: 0,
    marginHorizontal: 'auto',
  },
  contentContainer: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    justifyContent: 'center',
  },
  item: {
    padding: 10,
    height: 38,
    letterSpacing: 0.6,
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
    color: '#777777',
    textAlign: 'center',
  },
});

interface ItemProps {
  word: string;
}

const Item: React.FC<ItemProps> = ({word}) => {
  const {isHorizontal} = useListOrientation();
  const textItemStyle = !isHorizontal && styles.itemVertical;

  if (isBreak(word)) {
    return (
      <View style={StyleSheet.flatten([styles.item, styles.breakItem])}>
        <Text style={styles.breakText}>{word}</Text>
      </View>
    );
  }

  return (
    <View style={StyleSheet.flatten([styles.item, textItemStyle])}>
      <WordWithStrongFirstCap text={word} />
    </View>
  );
};

const renderItem = ({item}: {item: string}) => <Item word={item} />;

interface SpellWordsProps {
  words: string[];
}

export const SpellWords = ({words}: SpellWordsProps) => {
  const {isHorizontal} = useListOrientation();

  if (isHorizontal) {
    return (
      <View style={styles.container}>
        <KeyboardAwareScrollView
          contentContainerStyle={styles.contentContainer}>
          {words.map((word, index) => (
            <Item word={word} key={`${index} ${word}`} />
          ))}
        </KeyboardAwareScrollView>
      </View>
    );
  }

  return (
    <View style={styles.container}>
      <KeyboardAwareFlatList
        style={styles.listVertical}
        data={words}
        renderItem={renderItem}
        keyExtractor={(word, index) => `${index} ${word}`}
        horizontal={false}
      />
    </View>
  );
};
