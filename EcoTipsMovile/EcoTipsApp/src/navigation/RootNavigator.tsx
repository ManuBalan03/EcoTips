import React from 'react';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { View, Text } from 'react-native';
import AuthTabs from './authTabs'; // asegúrate que la ruta esté bien
import { RootStackParamList } from './types/RootStackParamList';

type RootNavigatorProps = {
  isLoggedIn: boolean;
  setIsLoggedIn: (v: boolean) => void;
};

const Stack = createNativeStackNavigator<RootStackParamList>();

export default function RootNavigator({ isLoggedIn, setIsLoggedIn }: RootNavigatorProps) {
  return (
    <Stack.Navigator screenOptions={{ headerShown: false }}>
      {!isLoggedIn ? (
        <Stack.Screen name="AuthTabs">
          {() => <AuthTabs setIsLoggedIn={setIsLoggedIn} />}
        </Stack.Screen>
      ) : (
        <Stack.Screen name="Home">
          {() => (
            <View>
              <Text>Bienvenido</Text>
            </View>
          )}
        </Stack.Screen>
      )}
    </Stack.Navigator>
  );
}
