import {useNavigation} from '@react-navigation/native';
import * as React from 'react';
import {StyleSheet, Text, View, Pressable} from 'react-native';

const styles = StyleSheet.create({
  wrapperRow: {
    paddingHorizontal: 10,
    flexDirection: 'row',
    justifyContent: 'flex-end',
  },
  button: {
    paddingVertical: 7,
    paddingHorizontal: 10,
  },
  text: {
    fontSize: 12,
    color: '#333333',
  },
});

export const AboutButton: React.FC = () => {
  const navigation = useNavigation();

  return (
    <View style={styles.wrapperRow}>
      <Pressable
        style={styles.button}
        /* @ts-ignore */
        onPress={() => navigation.navigate('AboutModal')}>
        <Text style={styles.text}>About</Text>
      </Pressable>
    </View>
  );
};
