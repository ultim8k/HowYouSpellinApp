import {useNavigation} from '@react-navigation/native';
import * as React from 'react';
import {StyleSheet, Text, View, Pressable} from 'react-native';

const styles = StyleSheet.create({
  aboutButton: {
    paddingVertical: 7,
    paddingHorizontal: 20,
    flexDirection: 'row',
    justifyContent: 'flex-end',
  },
  text: {
    fontSize: 12,
    color: '#333333',
  },
});

export const AboutButton: React.FC = () => {
  const navigation = useNavigation();

  return (
    <View style={styles.aboutButton}>
      {/* @ts-ignore */}
      <Pressable onPress={() => navigation.navigate('AboutModal')}>
        <Text style={styles.text}>About</Text>
      </Pressable>
    </View>
  );
};
