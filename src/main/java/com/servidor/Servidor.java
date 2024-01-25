package com.servidor;

import com.util.Mensaje;
import com.util.Usuario;
import com.servidor.hilos.GestionUsuario;
import lombok.Getter;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Servidor {
    static final int PORT = 6000;

    private static final List<Usuario> usuarios = new ArrayList<>();
    private static final List<GestionUsuario> clientesConectados = new ArrayList<>();
    @Getter
    private static final List<Mensaje> mensjaes = new ArrayList<>();
    protected static boolean funcionando = true;

    public static boolean esUsuarioValido(Usuario usuarioNuevo) {
        if (usuarioNuevo.getNombreUsuario() != null && usuarios.isEmpty() && !usuarioNuevo.getNombreUsuario().isBlank()){
            return true;
        }

        for (Usuario u : usuarios) {
            if (u == null || usuarioNuevo == null ||usuarioNuevo.getNombreUsuario().isBlank()||usuarioNuevo.getNombreUsuario().isEmpty()||u.getNombreUsuario().equalsIgnoreCase(usuarioNuevo.getNombreUsuario())) {
                return false;
            }
        }
        return true;
    }
    public static void addUser(Usuario u){
        usuarios.add(u);
    }
    public static void removeUser(Usuario u){
        usuarios.remove(u);
    }
    public static void addMensaje(Mensaje m){
        mensjaes.add(m);
        for(GestionUsuario h : clientesConectados){
            h.enviarMensaje(m);
        }

    }
    public static ArrayList<String> getUserList(){
        ArrayList<String> result = new ArrayList<>();
        for (Usuario u : usuarios){
            result.add(u.getNombreUsuario());
        }
        return result;
    }


    public static void main(String[] args) {
        try {
            ServerSocket servidor = new ServerSocket(PORT);
            while (funcionando) {
                GestionUsuario u = new GestionUsuario(servidor.accept());
                Thread nuevoHiloCLiente = new Thread(u);
                clientesConectados.add(u);
                nuevoHiloCLiente.start();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void removeClienteConectado(GestionUsuario gestionUsuario) {
        clientesConectados.remove(gestionUsuario);
    }
}
