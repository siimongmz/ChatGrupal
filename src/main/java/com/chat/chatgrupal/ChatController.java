package com.chat.chatgrupal;

import com.util.Mensaje;
import com.util.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.Getter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ChatController {
    @FXML
    public ObservableList<String> nombres = FXCollections.observableArrayList();
    @FXML @Getter
    public TextArea textAreaChat;
    @FXML
    public TextArea textAreaEnvio;
    @FXML
    private ListView<String> usuarios;

    @FXML
    private TextField textoUsuario;
    @FXML
    protected void comprobarInicioSesion(){
        Usuario usuario = new Usuario(textoUsuario.getText());
        try {
            ObjectOutputStream salida = ChatApplication.getSalida();
            ObjectInputStream entrada = ChatApplication.getEntrada();
            //Enviamos el objeto al servidor
            salida.writeObject(usuario);
            if (entrada.readInt()==1) {
                ChatApplication.setUsuario(usuario);
                Stage stage = (Stage) textoUsuario.getScene().getWindow();
                stage.close();
            } else {
                Alert alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setTitle("Error");
                alerta.setHeaderText(null); // Puedes establecer un encabezado si lo deseas
                alerta.setContentText("El Usuario ya existe");

                alerta.showAndWait();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
    @FXML
    protected void enviarMensaje(){
        ObjectOutputStream salida = ChatApplication.getSalida();
        ObjectInputStream entrada = ChatApplication.getEntrada();

        try {
            salida.writeObject(new Mensaje(textAreaEnvio.getText(),ChatApplication.getUsuario()));
            salida.flush();
            textAreaEnvio.setText("");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}