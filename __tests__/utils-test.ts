import {isBreak, replaceCharWithSpellWord} from '../src/utils';
import {charsMap, letters, numbers, symbols} from '../src/constants/charsMap';

describe('charsMap', () => {
  it('spells all 26 letters with the ICAO alphabet', () => {
    expect(Object.keys(letters)).toHaveLength(26);
    expect(letters.A).toBe('Alfa');
    expect(letters.J).toBe('Juliet');
    expect(letters.N).toBe('November');
    expect(letters.X).toBe('X-ray');
    expect(letters.Z).toBe('Zulu');
  });

  it('spells every digit 0-9', () => {
    ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9'].forEach(digit => {
      expect(numbers[digit as keyof typeof numbers]).toBeTruthy();
    });
  });

  it('has no empty spellings', () => {
    Object.values(charsMap).forEach(word => expect(word.trim()).not.toBe(''));
  });

  it('includes the supported symbols', () => {
    expect(Object.keys(symbols).sort()).toEqual([',', '-', '.', '_']);
  });
});

describe('replaceCharWithSpellWord', () => {
  it('maps letters', () => {
    expect(replaceCharWithSpellWord('H')).toBe('Hotel');
    expect(replaceCharWithSpellWord('I')).toBe('India');
  });

  it('maps digits', () => {
    expect(replaceCharWithSpellWord('7')).toBe('Seven');
  });

  it('maps symbols', () => {
    expect(replaceCharWithSpellWord('-')).toBe('- dash -');
    expect(replaceCharWithSpellWord('.')).toBe('- full stop -');
    expect(replaceCharWithSpellWord(',')).toBe('- comma -');
    expect(replaceCharWithSpellWord('_')).toBe('- underscore -');
  });

  it('maps a space to the break marker', () => {
    expect(replaceCharWithSpellWord(' ')).toBe('- break -');
  });

  it('returns N/A for unsupported characters', () => {
    expect(replaceCharWithSpellWord('@' as never)).toBe('N/A');
    expect(replaceCharWithSpellWord('é' as never)).toBe('N/A');
    expect(replaceCharWithSpellWord('😀' as never)).toBe('N/A');
  });

  it('is case sensitive, so callers must upper-case first', () => {
    expect(replaceCharWithSpellWord('h' as never)).toBe('N/A');
  });
});

describe('isBreak', () => {
  it('recognises the break marker only', () => {
    expect(isBreak('- break -')).toBe(true);
    expect(isBreak('Hotel')).toBe(false);
    expect(isBreak('')).toBe(false);
  });
});
