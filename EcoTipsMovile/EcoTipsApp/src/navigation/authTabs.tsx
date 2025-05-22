import React from 'react';
import { createMaterialTopTabNavigator } from '@react-navigation/material-top-tabs';
import LoginScreen from '../screens/LoginScreen';
import SignupScreen from '../screens/SignUp';

const Tab = createMaterialTopTabNavigator();

export default function AuthTabs({ setIsLoggedIn }: { setIsLoggedIn: (v: boolean) => void }) {
  return (
    <Tab.Navigator
      screenOptions={{
        tabBarActiveTintColor: '#fff',
        tabBarLabelStyle: { fontWeight: 'bold' },
        tabBarStyle: { backgroundColor: '#0984e3' },
      }}
    >
      <Tab.Screen name="Login">
        {() => <LoginScreen setIsLoggedIn={setIsLoggedIn} />}
      </Tab.Screen>
      <Tab.Screen name="Register" component={SignupScreen} />
    </Tab.Navigator>
  );
}
