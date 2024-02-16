import {charsMap, type CharsMap} from '../constants/charsMap';

export const replaceCharWithSpellWord = (char: keyof CharsMap): string =>
  charsMap[char] || 'N/A';

export const isBreak = (word: string): boolean => word === charsMap[' '];
