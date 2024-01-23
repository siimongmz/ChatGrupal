package com.server.util;

import com.chat.util.Usuario;
import com.server.Server;

import java.io.*;
import java.net.Socket;

public class GestionUsuario implements Runnable {
    Socket socket;
    Usuario usuario;
    ObjectInputStream entrada;
    ObjectOutputStream salida;

    public GestionUsuario(Socket socket) {
        this.socket = socket;
        try {
            this.salida = new ObjectOutputStream(socket.getOutputStream());
            this.entrada = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("Error en la salida o entrada");
        }
    }

    @Override
    public void run() {
        usuario = pedirUsuario();
        while (usuario == null){
            usuario = pedirUsuario();
        }
        Server.addUser(usuario);
        System.out.println(Server.getUserList());

    }

    public Usuario pedirUsuario() {
        Usuario usuario;
        try {
            usuario = (Usuario) entrada.readObject();
            if (usuario != null && Server.esUsuarioValido(usuario)) {
                salida.writeInt(1);
                salida.flush();
                return usuario;

            } else {
                salida.writeInt(0);
                salida.flush();
            }

        } catch (IOException e) {
            System.out.println("IOException");
        } catch (ClassNotFoundException e) {
            System.out.println("Clase no encontrada");
        }


        return null;
    }
}
