package com.chat.hilos;

import com.chat.chatgrupal.ChatApplication;
import com.util.Mensaje;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class GestionCliente implements Runnable{
    public TextArea textAreaEnvio;
    ObjectOutputStream salida = ChatApplication.getSalida();
    ObjectInputStream entrada = ChatApplication.getEntrada();
    private boolean funcionando = true;
    public GestionCliente(TextArea textArea){
        this.textAreaEnvio = textArea;
    }
    @Override
    public void run() {
        //recibirMensajes();
        try {
        while (funcionando){

                if(ChatApplication.server.getInputStream().available() > 0) {
                    tratarObjetoRecivido(entrada.readObject());
                }



        }
        salida.writeObject("termino");
        salida.flush();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        System.out.println("termino");
    }

    private void tratarObjetoRecivido(Object o){
        if (o instanceof ArrayList<?>){
            recibirListaUsuarios((ArrayList<String>)o);
        }else if(o instanceof Mensaje){
            recibirMensaje((Mensaje) o);
        }
    }

    private void recibirListaUsuarios(ArrayList<String> usuarios) {

    }
    private void recibirMensaje(Mensaje m){
        textAreaEnvio.appendText("\n"+m.getUsuario().getNombreUsuario()+": "+m.getContenido());
    }

    public void terminarProceso(){
        funcionando = false;
    }


}
