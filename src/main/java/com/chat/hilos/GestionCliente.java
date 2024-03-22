package com.chat.hilos;

import com.chat.chatgrupal.ChatApplication;
import com.util.Mensaje;
import com.util.Usuario;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class GestionCliente implements Runnable{
    public VBox vBoxChat;
    public ScrollPane scrollMensajes;
    private ObservableList<Usuario> usuarios;
    private Usuario usuario;
    ObjectOutputStream salida = ChatApplication.getSalida();
    ObjectInputStream entrada = ChatApplication.getEntrada();
    private final String CODIGO_FIN = "termino";
    private boolean funcionando = true;
    public GestionCliente(ScrollPane scrollMensajes,VBox vBox, ObservableList<Usuario> usuarios,Usuario usuario){
        this.scrollMensajes = scrollMensajes;
        this.vBoxChat = vBox;
        this.usuarios = usuarios;
        this.usuario = usuario;
        scrollMensajes.vvalueProperty().bind(vBoxChat.heightProperty());
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
            recibirListaUsuarios((ArrayList<Usuario>)object);
        }else if(object instanceof Mensaje){
            recibirMensaje((Mensaje) object);
        }
    }

    /**
     * Se encarga de renovar la lista de los usuarios conectados
     * @param usuariosRecibios la lista por la cual se remplaza el contenido la anterior
     */
    private void recibirListaUsuarios(ArrayList<Usuario> usuariosRecibios) {
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
    public void recibirMensaje(Mensaje mensaje){
        //Creamos un espacio horizontal donde irá la burbuja del mesnaje
        HBox contenedorMensaje = new HBox();
        HBox.setHgrow(contenedorMensaje,Priority.ALWAYS);

        Label labelMensaje = new Label();
        if(mensaje.getUsuario().getNombreUsuario().equals(usuario.getNombreUsuario())){
            contenedorMensaje.setAlignment(Pos.BOTTOM_RIGHT);
            labelMensaje.setText(mensaje.getContenido());
            labelMensaje.setStyle("-fx-background-color: #7ad17a;" +
                    "-fx-background-radius: 20 20 0 20;" +
                    "-fx-font-size: 20px;" +
                    "-fx-padding: 10px;");
        }else{
            contenedorMensaje.setAlignment(Pos.BOTTOM_LEFT);
            labelMensaje.setStyle("-fx-background-color: #c5c4c4;\n" +
                    "-fx-background-radius: 20 20 20 0;"+
                    "-fx-font-size: 20px;" +
                    "-fx-padding: 10px;");
            labelMensaje.setText(mensaje.getUsuario().getNombreUsuario()+": "+mensaje.getContenido());
        }
        HBox.setHgrow(labelMensaje,Priority.ALWAYS);
        contenedorMensaje.getChildren().add(labelMensaje);
        HBox.setHgrow(contenedorMensaje, Priority.ALWAYS);
        Platform.runLater(()->{
            vBoxChat.getChildren().add(contenedorMensaje);
        });
    }

    /**
     * Actualiza el booleano para acabar con el bucle principal haciendo que el programa finalice
     */
    public void terminarProceso(){
        funcionando = false;
    }


}
