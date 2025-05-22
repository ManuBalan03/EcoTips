import React, { useState } from "react";
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  StyleSheet,
  Alert,
  ActivityIndicator,
} from "react-native";
import { registerUser } from '../../Api/Service/UserService'
import { useNavigation } from "@react-navigation/native";

export default function SignupScreen() {
  const navigation = useNavigation();
  const [formData, setFormData] = useState({
    email: "",
    nombre: "",
    contraseña: "",
  });
  const [error, setError] = useState<string | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleChange = (name: string, value: string) => {
    setFormData({ ...formData, [name]: value });
    setError(null);
  };

  const handleSubmit = async () => {
    setIsSubmitting(true);
    setError(null);

    try {
      await registerUser(formData);
      Alert.alert("Éxito", "Usuario registrado exitosamente");
      navigation.navigate("Login" as never); // asegúrate que "Login" sea el nombre correcto de la ruta
    } catch (error: any) {
      setError(error.message || "Ocurrió un error inesperado");
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Registrarse</Text>

      {error && <Text style={styles.errorText}>{error}</Text>}

      <TextInput
        style={styles.input}
        placeholder="Email"
        value={formData.email}
        onChangeText={(text) => handleChange("email", text)}
        keyboardType="email-address"
        autoCapitalize="none"
      />

      <TextInput
        style={styles.input}
        placeholder="Nombre"
        value={formData.nombre}
        onChangeText={(text) => handleChange("nombre", text)}
      />

      <TextInput
        style={styles.input}
        placeholder="Contraseña"
        value={formData.contraseña}
        onChangeText={(text) => handleChange("contraseña", text)}
        secureTextEntry
      />

      <TouchableOpacity style={styles.button} onPress={handleSubmit} disabled={isSubmitting}>
        {isSubmitting ? (
          <ActivityIndicator color="#45DF88" />
        ) : (
          <Text style={styles.buttonText}>Registrarse</Text>
        )}
      </TouchableOpacity>

      <Text style={styles.footer}>© 2025 Tu Empresa</Text>
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
