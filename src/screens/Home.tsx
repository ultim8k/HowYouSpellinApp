import React from 'react';
import {SafeAreaView, StyleSheet, View} from 'react-native';

import {SpellWords} from '../components/SpellWords';
import {InputForm} from '../components/InputForm';

import {replaceCharWithSpellWord} from '../utils';
import {fontSizes} from '../constants/fontSizes';
import {type CharsMap} from '../constants/charsMap';
import {useTheme} from '../hooks/useTheme';
import {FavouritesPills} from '../components/FavouritesPills';
import {useInputBarPosition} from '../hooks/useInputBarPosition';
import {Intro} from '../components/Intro';
import {useInputText} from '../hooks/useInputText';

const styles = StyleSheet.create({
  app: {
    display: 'flex',
    flexGrow: 1,
    height: '100%',
    width: '100%',
    maxWidth: '100%',
  },
  welcomeContent: {
    marginVertical: 20,
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    // flex: 1,
  },
  contentContainer: {
    paddingVertical: 15,
    paddingHorizontal: 15,
    display: 'flex',
    flex: 1,
  },
  title: {
    fontWeight: 'bold',
    fontSize: fontSizes.xlarge,
    marginBottom: 10,
    textAlign: 'center',
  },
  description: {
    textAlign: 'center',
    marginBottom: 20,
  },
  topMenu: {
    display: 'flex',
    flexDirection: 'row',
    justifyContent: 'space-between',
    paddingVertical: 10,
  },
});

export const Home: React.FC = () => {
  const {isBottom} = useInputBarPosition();
  const {text, updateInputText} = useInputText();
  const spellWords = text
    .toUpperCase()
    .split('')
    .map(char => replaceCharWithSpellWord(char as keyof CharsMap));
  const handleTextChange = (inputText: string): void =>
    updateInputText(inputText);
  const {isDark, styles: themeStyles} = useTheme();

  console.log('isDark', isDark);

  return (
    <SafeAreaView style={[styles.app, themeStyles.backgroundPrimary]}>
      <View style={styles.contentContainer}>
        {!isBottom && (
          <InputForm text={text} handleTextChange={handleTextChange} />
        )}
        {text ? (
          <SpellWords words={spellWords} />
        ) : (
          <View style={styles.welcomeContent}>
            <Intro />
            <FavouritesPills />
          </View>
        )}
        {isBottom && (
          <InputForm text={text} handleTextChange={handleTextChange} />
        )}
      </View>
    </SafeAreaView>
  );
};
