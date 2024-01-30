package com.chat.hilos;

import com.chat.chatgrupal.ChatApplication;
import com.util.Mensaje;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class GestionCliente implements Runnable{
    public TextArea textAreaEnvio;
    private ObservableList<String> usuarios;
    ObjectOutputStream salida = ChatApplication.getSalida();
    ObjectInputStream entrada = ChatApplication.getEntrada();
    private final String CODIGO_FIN = "termino";
    private boolean funcionando = true;
    public GestionCliente(TextArea textArea, ObservableList<String> usuarios){
        this.textAreaEnvio = textArea;
        this.usuarios = usuarios;
    }
    @Override
    public void run() {
        try {
        while (funcionando){

                if(ChatApplication.getServer().getInputStream().available() > 0) {
                    gestionarEntrada(entrada.readObject());
                }



        }
        salida.writeObject(CODIGO_FIN);
        salida.flush();
        salida.close();
        entrada.close();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Recibe un objeto y en funcion del tipo se tratará de distinta forma
     * @param object El objeto sobre el cual queramos hacer la clasificacion
     */
    private void gestionarEntrada(Object object){
        if (object instanceof ArrayList<?>){
            recibirListaUsuarios((ArrayList<String>)object);
        }else if(object instanceof Mensaje){
            recibirMensaje((Mensaje) object);
        }
    }

    /**
     * Se encarga de renovar la lista de los usuarios conectados
     * @param usuariosRecibios la lista por la cual se remplaza el contenido la anterior
     */
    private void recibirListaUsuarios(ArrayList<String> usuariosRecibios) {
       /*
        *  El uso de Platform.runLater() para solucionar el error Not on FX application thread
        *  es una idea sacada de https://stackoverflow.com/questions/21083945
        *  esto fuerza a que las operaciones sobre los componentes se realicen en el hilo de ejecucion
        *  de la aplicacion de JavaFX
        */

        Platform.runLater(()->{
            usuarios.clear();
            usuarios.addAll(usuariosRecibios);
        });

    }

    /**
     * Agrega el mensaje recibido al TextArea del cliente para mostrarlo
     * @param mensaje el mensaje que se agregará
     */
    private void recibirMensaje(Mensaje mensaje){
        textAreaEnvio.appendText("\n"+mensaje.getUsuario().getNombreUsuario()+": "+mensaje.getContenido());
    }

    /**
     * Actualiza el booleano para acabar con el bucle principal haciendo que el programa finalice
     */
    public void terminarProceso(){
        funcionando = false;
    }


}
