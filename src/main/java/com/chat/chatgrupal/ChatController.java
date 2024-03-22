package com.chat.chatgrupal;

import com.util.Mensaje;
import com.util.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Getter;

import java.io.*;
import java.util.Base64;

public class ChatController {

    @FXML @Getter
    public VBox vBoxChat;
    @FXML @Getter
    public ScrollPane scrollMensajes;
    @FXML
    public TextArea textAreaEnvio;
    @FXML @Getter
    private ListView<Usuario> usuarios;

    @FXML
    private TextField textoUsuario;
    @FXML
    private Label filePathLabel;

    /**
     * Metodo encargado de gestionar el inicio de sesion, enviara el nombre de usuario escrito al servidor y mostrará una alerta en caso de que no sea valido y continuará con la ejecucion asignando el nuevo usuario al cliente en caso de que si
     */
    @FXML
    protected void comprobarInicioSesion(){

        Usuario usuario = new Usuario(textoUsuario.getText(),encoder(filePathLabel.getText()));
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
    private void openFilePicker() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo");

        // Configurar el filtro de extensión si lo necesitas
        // fileChooser.getExtensionFilters().addAll(
        //         new FileChooser.ExtensionFilter("Text Files", "*.txt"),
        //         new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
        //         new FileChooser.ExtensionFilter("All Files", "*.*")
        // );

        // Mostrar el diálogo de selección de archivo
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            // Actualizar la etiqueta con la ruta del archivo seleccionado
            filePathLabel.setText(selectedFile.getAbsolutePath());
        } else {
            filePathLabel.setText("Ningún archivo seleccionado");
        }
    }
    public static String encoder(String imagePath) {
        String base64Image = "";
        File file = new File(imagePath);
        try (FileInputStream imageInFile = new FileInputStream(file)) {
            // Reading a Image file from file system
            byte imageData[] = new byte[(int) file.length()];
            imageInFile.read(imageData);
            base64Image = Base64.getEncoder().encodeToString(imageData);
        } catch (FileNotFoundException e) {
            System.out.println("Image not found" + e);
        } catch (IOException ioe) {
            System.out.println("Exception while reading the Image " + ioe);
        }
        return base64Image;
    }
    /**
     * Metodo encargado de enviar un mensaje al servidor para que este se encarge de gestionarlo, borrara el texto escrito en el TextArea de input y se ejecutará al presionar el boton de envio
     */
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