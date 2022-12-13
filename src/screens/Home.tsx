import React, {useState} from 'react';
import {SafeAreaView, StyleSheet, Text, View} from 'react-native';

import {MadeBy} from '../components/MadeBy';
import {SpellWords} from '../components/SpellWords';
import {InputForm} from '../components/InputForm';

import {replaceCharWithSpellWord} from '../utils';

const styles = StyleSheet.create({
  app: {
    backgroundColor: '#eaeaea',
    display: 'flex',
    flexGrow: 1,
    height: '100%',
    width: '100%',
    maxWidth: '100%',
  },
  header: {
    marginVertical: 20,
    paddingHorizontal: 20,
  },
  title: {
    fontWeight: 'bold',
    fontSize: 25,
    marginVertical: 10,
    textAlign: 'center',
  },
  description: {
    textAlign: 'center',
  },
});

const Home: React.FC = () => {
  const [text, setText] = useState<string>('RONCTTLA'); // Try "RONCTTLA" or "220040DL"
  /* @ts-ignore */
  const spellWords = text.toUpperCase().split('').map(replaceCharWithSpellWord);
  const handleTextChange = (inputText: string): void => setText(inputText);

  return (
    <SafeAreaView style={styles.app}>
      <MadeBy />
      <View style={styles.header}>
        <Text style={styles.title}>How you spellin?</Text>
        <Text style={styles.description}>
          Convert text to spell words using the International Radiotelephony
          Spelling Alphabet.
        </Text>
      </View>
      <InputForm text={text} handleTextChange={handleTextChange} />
      <SpellWords words={spellWords} />
    </SafeAreaView>
  );
};

export default Home;
