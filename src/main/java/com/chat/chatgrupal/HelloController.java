package com.chat.chatgrupal;

import com.chat.util.Usuario;
import javafx.collections.ObservableArray;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class HelloController {
    @FXML
    private ListView<Usuario> usuarios;
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
    @FXML
    protected ListView getUsuarios() {
        return usuarios;
    }
}