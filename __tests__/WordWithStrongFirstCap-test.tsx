import React from 'react';
import {Text} from 'react-native';

import {WordWithStrongFirstCap} from '../src/components/WordWithStrongFirstCap';
import {renderAsync, textOf} from './helpers/render';

describe('WordWithStrongFirstCap', () => {
  it('renders the whole word', async () => {
    const renderer = await renderAsync(<WordWithStrongFirstCap text="Hotel" />);

    expect(textOf(renderer.toJSON() as never)).toBe('Hotel');
  });

  it('emphasises only the first letter', async () => {
    const renderer = await renderAsync(<WordWithStrongFirstCap text="Hotel" />);
    const [, strong] = renderer.root.findAllByType(Text);

    expect(strong.props.style).toMatchObject({fontWeight: '600'});
    expect(textOf(strong as never)).toBe('H');
  });

  it('ignores surrounding whitespace', async () => {
    const renderer = await renderAsync(
      <WordWithStrongFirstCap text="  Echo " />,
    );

    expect(textOf(renderer.toJSON() as never)).toBe('Echo');
  });
});
