package com.server;

import com.chat.util.Usuario;
import com.server.util.GestionUsuario;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    static final int PORT = 6000;

    private static final List<Usuario> usuarios = new ArrayList<>();
    protected static boolean funcionando = true;

    public static boolean esUsuarioValido(Usuario usuarioNuevo) {
        if (usuarioNuevo.getNombreUsuario() != null && usuarios.isEmpty() && !usuarioNuevo.getNombreUsuario().isBlank()){
            return true;
        }
        for (Usuario u : usuarios) {
            if (u != null && usuarioNuevo != null &&!usuarioNuevo.getNombreUsuario().isBlank()&&!usuarioNuevo.getNombreUsuario().isEmpty()&& !u.getNombreUsuario().equalsIgnoreCase(usuarioNuevo.getNombreUsuario())) {
                return true;
            }
        }
        return false;
    }
    public static void addUser(Usuario u){
        usuarios.add(u);
    }
    public static String getUserList(){
        String result ="";
        for (Usuario u : usuarios){
            result += "["+u.getNombreUsuario()+"] ";
        }
        return result;
    }

    public static void main(String[] args) {
        try {
            ServerSocket servidor = new ServerSocket(PORT);
            while (funcionando) {
                new Thread(new GestionUsuario(servidor.accept())).start();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
