import {MMKV} from 'react-native-mmkv';

const storage = new MMKV({
  id: 'how-you-spellin-favourites-storage',
  encryptionKey: 'how-you-spellin',
});

export const getFavourites = async (): Promise<string[]> => {
  return storage.getAllKeys() || [];
};

export const getFavouritesWithContent = async (): Promise<
  {title: string; text: string}[]
> => {
  return (
    storage.getAllKeys().map(key => {
      return {
        title: key,
        text: storage.getString(key) || '',
      };
    }) || []
  );
};

export const getFavouriteByKey = async (key: string): Promise<any> =>
  storage.getString(key);

export const isTextInFavourites = async (text: string): Promise<boolean> => {
  return storage.getAllKeys().includes(text);
};

export const addFavourite = async ({
  text,
  name,
}: {
  text: string;
  name?: string;
}): Promise<void> => {
  const key = name || text.replace(/\s/g, '-').toLowerCase();

  storage.set(key, text);
};

export const deleteFavouriteByKey = async (key: string): Promise<void> =>
  storage.delete(key);

export const deleteAllFavourites = async (confirm: boolean): Promise<void> => {
  if (!confirm) {
    return;
  }

  storage.clearAll();
};
