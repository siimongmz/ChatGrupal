package com.chat.chatgrupal;
import com.chat.util.Usuario;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import lombok.Getter;
import lombok.Setter;

public class ChatApplication extends Application {
    @Getter @Setter
    private static Usuario usuario;

    private static Socket server;
    @Getter
    private static ObjectInputStream entrada;
    @Getter
    private static ObjectOutputStream salida;


    @Override
    public void start(Stage stage) throws IOException {
        conexionServidor();
        mostrarInicioSesion();
        if (usuario != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(ChatApplication.class.getResource("general-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 500, 800);

            stage.setTitle("ChatGrupal");
            stage.setScene(scene);
            stage.show();
            System.out.println("fin");
        }
    }
    public void conexionServidor(){
        try {
            server = new Socket("localhost",6000);
            salida = new ObjectOutputStream(server.getOutputStream());
            entrada = new ObjectInputStream(server.getInputStream());

        } catch (IOException e) {
            throw new RuntimeException("Error al conectar con el servidor");
        }

    }
    public void mostrarInicioSesion() throws IOException {
        Stage inicioSesion = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(ChatApplication.class.getResource("inicio-sesion-view.fxml"));
        Scene sceneInicioSesion = new Scene(fxmlLoader.load(), 200, 200);
        inicioSesion.initModality(Modality.APPLICATION_MODAL);
        inicioSesion.setTitle("Inicio de Sesion");
        inicioSesion.setScene(sceneInicioSesion);
        inicioSesion.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }

}