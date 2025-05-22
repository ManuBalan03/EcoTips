import React, { useState } from 'react';
import { View, TextInput, Button, Text, StyleSheet, ActivityIndicator } from 'react-native';
import { loginUser } from '../../Api/Service/UserService';
import { RootStackParamList } from '../../src/navigation/types/RootStackParamList';
import { useNavigation } from '@react-navigation/native';
import { NativeStackNavigationProp } from '@react-navigation/native-stack';


type Props = {
  setIsLoggedIn: (v: boolean) => void;
};

type LoginScreenNavigationProp = NativeStackNavigationProp<RootStackParamList, 'Login'>;

export default function LoginScreen({ setIsLoggedIn }: Props) {
  const navigation = useNavigation<LoginScreenNavigationProp>();

  const [loginData, setLoginData] = useState({ email: '', password: '' });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

const handleLogin = async () => {
  setLoading(true);
  setError('');

  try {
    const response = await loginUser(loginData);
    console.log('Respuesta del login:', response);

    // Si usas axios
    if (response.status === 200) {
      setIsLoggedIn(true);
    } else {
      setError('Credenciales incorrectas');
    }
  } catch (err) {
    console.log('Error en login:', err);
    setError('Credenciales incorrectas');
  } finally {
    setLoading(false);
  }
};
  return (
    <View style={styles.container}>
      <Text>Email:</Text>
      <TextInput
        style={styles.input}
        value={loginData.email}
        onChangeText={(text) => setLoginData({ ...loginData, email: text })}
        keyboardType="email-address"
        autoCapitalize="none"
      />

      <Text>Contraseña:</Text>
      <TextInput
        style={styles.input}
        value={loginData.password}
        onChangeText={(text) => setLoginData({ ...loginData, password: text })}
        secureTextEntry
      />

      {error !== '' && <Text style={styles.error}>{error}</Text>}
      {loading ? <ActivityIndicator /> : <Button title="Iniciar sesión" onPress={handleLogin} />}
    </View>
  );
}


const styles = StyleSheet.create({
  container: {
    maxWidth: 400,
    width: "90%",
    alignSelf: "center",
    marginTop: 40,
    textAlign: "center",
  },
   error: {
    color: 'red',
    marginBottom: 10,
  },
  title: {
    fontSize: 24,
    fontWeight: "bold",
    textAlign: "center",
    marginBottom: 20,
  },
  input: {
    width: "100%",
    padding: 12,
    marginVertical: 10,
    borderRadius: 8,
    borderWidth: 1,
    borderColor: "#ccc",
  },
  button: {
    width: "100%",
    padding: 12,
    backgroundColor: "white",
    borderWidth: 2,
    borderColor: "#45DF88",
    borderRadius: 12,
    alignItems: "center",
    marginTop: 10,
  },
  buttonText: {
    color: "#45DF88",
    fontWeight: "bold",
  },
  errorText: {
    color: "red",
    marginBottom: 10,
    textAlign: "center",
  },
  footer: {
    position: "absolute",
    bottom: 10,
    right: 10,
    fontSize: 12,
    color: "#888",
    textAlign: "right",
  },
});

