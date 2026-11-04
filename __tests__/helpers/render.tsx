import React from 'react';
import ReactTestRenderer from 'react-test-renderer';

export const renderAsync = async (
  element: React.ReactElement,
): Promise<ReactTestRenderer.ReactTestRenderer> => {
  let renderer!: ReactTestRenderer.ReactTestRenderer;

  await ReactTestRenderer.act(() => {
    renderer = ReactTestRenderer.create(element);
  });

  return renderer;
};

export const act = ReactTestRenderer.act;

/** All rendered text in the tree, concatenated in document order. */
export const textOf = (
  node: ReactTestRenderer.ReactTestRendererNode | null,
): string => {
  if (node === null) {
    return '';
  }

  if (typeof node === 'string') {
    return node;
  }

  return (node.children ?? []).map(textOf).join('');
};
