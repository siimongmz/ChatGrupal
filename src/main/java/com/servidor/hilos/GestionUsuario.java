package com.servidor.hilos;

import com.util.Mensaje;
import com.util.Usuario;
import com.servidor.Servidor;

import java.io.*;
import java.net.Socket;

public class GestionUsuario implements Runnable {
    Socket socket;
    Usuario usuario;
    boolean funcionando = true;
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
        //Se pide un usuario
        usuario = pedirUsuario();
        while (usuario == null) {
            usuario = pedirUsuario();
        }
        Servidor.addUser(usuario);
        //Se escucha la solicitud
        while (funcionando) {
            try {

                if (socket.getInputStream().available() > 0) {
                    gestionarEntrada(entrada.readObject());
                }

            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            //enviarUsuarios();
        }
    }

    public void gestionarEntrada(Object o) {
        if (o instanceof String) {
            leerDesconexiones((String) o);
        } else if (o instanceof Mensaje) {
            leerMensaje((Mensaje) o);
        }
    }

    private void leerDesconexiones(String s) {
        if (s.equals("termino")) {
            Servidor.removeUser(this.usuario);
            Servidor.removeClienteConectado(this);
            this.funcionando = false;
        }

    }

    private Usuario pedirUsuario() {
        Usuario usuario;
        try {
            usuario = (Usuario) entrada.readObject();
            if (usuario != null && Servidor.esUsuarioValido(usuario)) {
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

    private void enviarUsuarios() {
        try {
            salida.writeObject(Servidor.getUserList());
            salida.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void leerMensaje(Mensaje m) {
        Servidor.addMensaje(m);
        System.out.println("Mensaje: " +  m.getContenido());
    }

    public void enviarMensaje(Mensaje m) {
        try {
            salida.writeObject(m);
            salida.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
