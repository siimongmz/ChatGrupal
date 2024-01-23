package com.chat.chatgrupal;

import com.chat.util.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ChatController {
    @FXML
    private ListView<Usuario> usuarios;

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
            System.out.println("me quedo");
            if (entrada.readInt()==1) {
                System.out.println("aqui");
                ChatApplication.setUsuario(usuario);
                Stage stage = (Stage) textoUsuario.getScene().getWindow();
                stage.close();
            } else {
                System.out.println("aqui else");
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
    protected ListView getUsuarios() {
        return usuarios;
    }
}