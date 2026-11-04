/**
 * @format
 */

import React from 'react';
import {Linking, Switch, TextInput} from 'react-native';

import App from '../src/App';
import {legalLink, githubLink, webappLink} from '../src/constants/links';
import {act, renderAsync, textOf} from './helpers/render';

const typeText = async (
  renderer: Awaited<ReturnType<typeof renderAsync>>,
  value: string,
) => {
  const input = renderer.root.findByType(TextInput);

  await act(() => {
    input.props.onChangeText(value);
  });
};

const toggleVertical = async (
  renderer: Awaited<ReturnType<typeof renderAsync>>,
) => {
  const toggle = renderer.root.findByType(Switch);

  await act(() => {
    toggle.props.onValueChange(!toggle.props.value);
  });
};

const screenText = (renderer: Awaited<ReturnType<typeof renderAsync>>) =>
  textOf(renderer.toJSON() as never);

describe('Home screen', () => {
  it('renders the title, description and input', async () => {
    const renderer = await renderAsync(<App />);

    expect(screenText(renderer)).toContain('How you spellin?');
    expect(screenText(renderer)).toContain(
      'International Radiotelephony Spelling Alphabet',
    );
    expect(renderer.root.findByType(TextInput).props.placeholder).toBe(
      'Type some text',
    );
  });

  it('spells typed text, case-insensitively', async () => {
    const renderer = await renderAsync(<App />);

    await typeText(renderer, 'hi');

    expect(screenText(renderer)).toContain('Hotel');
    expect(screenText(renderer)).toContain('India');
  });

  it('spells digits and shows breaks between words', async () => {
    const renderer = await renderAsync(<App />);

    await typeText(renderer, 'a 1');

    const text = screenText(renderer);
    expect(text).toContain('Alfa');
    expect(text).toContain('- break -');
    expect(text).toContain('One');
  });

  it('shows N/A for unsupported characters', async () => {
    const renderer = await renderAsync(<App />);

    await typeText(renderer, '@');

    expect(screenText(renderer)).toContain('N/A');
  });

  it('clears the spelling when the text is cleared', async () => {
    const renderer = await renderAsync(<App />);

    await typeText(renderer, 'hi');
    await typeText(renderer, '');

    expect(screenText(renderer)).not.toContain('Hotel');
  });

  it('keeps the spelling when switching to the vertical list', async () => {
    const renderer = await renderAsync(<App />);

    await typeText(renderer, 'hi');
    expect(renderer.root.findByType(Switch).props.value).toBe(false);

    await toggleVertical(renderer);

    expect(renderer.root.findByType(Switch).props.value).toBe(true);
    expect(screenText(renderer)).toContain('Hotel');
    expect(screenText(renderer)).toContain('India');

    await toggleVertical(renderer);

    expect(renderer.root.findByType(Switch).props.value).toBe(false);
    expect(screenText(renderer)).toContain('Hotel');
  });

  it('handles long text', async () => {
    const renderer = await renderAsync(<App />);

    await typeText(
      renderer,
      'the quick brown fox jumps over the lazy dog'.repeat(5),
    );

    expect(screenText(renderer)).toContain('Whiskey');
  });
});

describe('About screen', () => {
  type Renderer = Awaited<ReturnType<typeof renderAsync>>;

  /** Presses the first pressable whose visible text is exactly `label`. */
  const press = async (renderer: Renderer, label: string) => {
    const target = renderer.root.find(
      node =>
        typeof node.props.onPress === 'function' &&
        textOf(node as never) === label,
    );

    await act(() => {
      target.props.onPress();
    });
  };

  const openAbout = async () => {
    const renderer = await renderAsync(<App />);

    await press(renderer, 'About');

    return renderer;
  };

  let openURL: jest.SpyInstance;

  beforeEach(() => {
    openURL = jest.spyOn(Linking, 'openURL').mockResolvedValue(undefined);
  });

  afterEach(() => {
    openURL.mockRestore();
  });

  it('opens from the About button and lists the links', async () => {
    const renderer = await openAbout();
    const text = screenText(renderer);

    expect(text).toContain('Web app');
    expect(text).toContain('Privacy Policy');
    expect(text).toContain('Source code on Github');
  });

  it.each([
    ['Web app', webappLink],
    ['Privacy Policy', legalLink],
    ['Source code on Github', githubLink],
  ])('opens %s in the browser', async (label, url) => {
    const renderer = await openAbout();

    await press(renderer, label);

    expect(openURL).toHaveBeenCalledWith(url);
  });
});
