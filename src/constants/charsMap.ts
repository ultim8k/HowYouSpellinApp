export const letters = {
  A: 'Alfa',
  B: 'Bravo',
  C: 'Charlie',
  D: 'Delta',
  E: 'Echo',
  F: 'Foxtrot',
  G: 'Golf',
  H: 'Hotel',
  I: 'India',
  J: 'Juliet',
  K: 'Kilo',
  L: 'Lima',
  M: 'Mike',
  N: 'November',
  O: 'Oscar',
  P: 'Papa',
  Q: 'Quebec',
  R: 'Romeo',
  S: 'Sierra',
  T: 'Tango',
  U: 'Uniform',
  V: 'Victor',
  W: 'Whiskey',
  X: 'X-ray',
  Y: 'Yankee',
  Z: 'Zulu',
};

export const numbers = {
  '0': 'Zero',
  '1': 'One',
  '2': 'Two',
  '3': 'Three',
  '4': 'Four',
  '5': 'Five',
  '6': 'Six',
  '7': 'Seven',
  '8': 'Eight',
  '9': 'Nine',
  '00': 'Hundred',
  '000': 'Thousand',
};
export const numbersExtraSpelling = {
  '0': 'Zero, nadazero',
  '1': 'One, unaone',
  '2': 'Two, bissotwo',
  '3': 'Three, terrathree',
  '4': 'Four, kartefour',
  '5': 'Five, pantafive',
  '6': 'Six, soxisix',
  '7': 'Seven, setteseven',
  '8': 'Eight, oktoeight',
  '9': 'Nine, novenine',
  '00': 'Hundred',
  '000': 'Thousand',
};

export const symbols = {
  // ".": "- decimal, point -",
  '-': '- dash -',
  '.': '- full stop -',
  ',': '- comma -',
  _: '- underscore -',
};

export const charsMap = {
  ...letters,
  ...numbers,
  ...symbols,
  ' ': '- break -',
} as const;

export type CharsMap = typeof charsMap;
