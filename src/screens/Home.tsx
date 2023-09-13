import React, {useState} from 'react';
import {Appearance, SafeAreaView, StyleSheet, Text, View} from 'react-native';

import {SpellWords} from '../components/SpellWords';
import {InputForm} from '../components/InputForm';

import {replaceCharWithSpellWord} from '../utils';
import {AboutButton} from '../components/AboutButton';
import {colors} from '../constants/colors';
import {fontSizes} from '../constants/fontSizes';

const styles = StyleSheet.create({
  app: {
    display: 'flex',
    flexGrow: 1,
    height: '100%',
    width: '100%',
    maxWidth: '100%',
  },
  appLight: {
    backgroundColor: colors.light,
  },
  appDark: {
    backgroundColor: colors.dark,
  },
  header: {
    marginVertical: 20,
    paddingHorizontal: 20,
  },
  title: {
    fontWeight: 'bold',
    fontSize: fontSizes.xlarge,
    marginVertical: 10,
    textAlign: 'center',
  },
  description: {
    textAlign: 'center',
  },
  aboutButton: {
    textAlign: 'right',
  },
  textDark: {
    color: colors.light,
  },
  textLight: {
    color: colors.black,
  },
});

export const Home: React.FC = () => {
  const [text, setText] = useState<string>('');
  /* @ts-ignore */
  const spellWords = text.toUpperCase().split('').map(replaceCharWithSpellWord);
  const handleTextChange = (inputText: string): void => setText(inputText);
  const colorScheme = Appearance.getColorScheme();

  const appColorStyle =
    colorScheme === 'dark' ? styles.appDark : styles.appLight;
  const textColorStyle =
    colorScheme === 'dark' ? styles.textDark : styles.textLight;

  return (
    <SafeAreaView style={[styles.app, appColorStyle]}>
      <AboutButton />
      <View style={styles.header}>
        <Text style={[styles.title, textColorStyle]}>How you spellin?</Text>
        <Text style={[styles.description, textColorStyle]}>
          Convert text to spell words using the International Radiotelephony
          Spelling Alphabet.
        </Text>
      </View>
      <InputForm text={text} handleTextChange={handleTextChange} />
      <SpellWords words={spellWords} />
    </SafeAreaView>
  );
};
