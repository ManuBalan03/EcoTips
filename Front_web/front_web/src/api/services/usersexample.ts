import api from "../axios";
import { Usuario } from "../types/userexample";

// Definimos una interfaz para un usuario

// Ejemplo de función para obtener usuarios
export const obtenerUsuarios = async (): Promise<Usuario[]> => {
  try {
    const response = await api.get("/usuarios");
    return response.data;
  } catch (error) {
    console.error("Error al obtener usuarios:", error);
    throw error;
  }
};
// Ejemplo de función para crear un usuario
export const crearUsuario = async (userData: Usuario): Promise<Usuario> => {
  try {
    const response = await api.post("/usuarios", userData);
    return response.data;
  } catch (error) {
    console.error("Error al crear usuario:", error);
    throw error;
  }
};

// Ejemplo de función para actualizar un usuario
export const actualizarUsuario = async (id: number, userData: Usuario): Promise<Usuario> => {
  try {
    const response = await api.put(`/usuarios/${id}`, userData);
    return response.data;
  } catch (error) {
    console.error("Error al actualizar usuario:", error);
    throw error;
  }
};

// Ejemplo de función para eliminar un usuario
export const eliminarUsuario = async (id: number): Promise<void> => {
  try {
    const response = await api.delete(`/usuarios/${id}`);
    return response.data;
  } catch (error) {
    console.error("Error al eliminar usuario:", error);
    throw error;
  }
};
