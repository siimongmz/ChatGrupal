package com.chat.chatgrupal;

import com.util.Usuario;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.util.Callback;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Base64;

public class UserListFactory implements Callback<ListView<Usuario>, ListCell<Usuario>> {

    @Override
    public ListCell<Usuario> call(ListView<Usuario> usuarioListView) {
        return new ListCell<>(){
            @Override
            protected void updateItem(Usuario usuario, boolean b) {
                super.updateItem(usuario, b);

                if (b || usuario == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    byte[] imageByteArray = Base64.getDecoder().decode(usuario.getBase64Perfil());
                    Image perfil = new Image(new ByteArrayInputStream(imageByteArray));
                    ImageView vistaImagen = new ImageView(perfil);
                    vistaImagen.setPreserveRatio(true);
                    vistaImagen.setFitHeight(50);
//                    vistaImagen.setFitWidth(50);
                    Circle clip = new Circle(25); // Radio del círculo
                    clip.setCenterX(25); // Posición X del círculo
                    clip.setCenterY(25); // Posición Y del círculo

                    // Establecer el efecto de recorte en la ImageView usando un Group
                    Group group = new Group();
                    group.getChildren().add(vistaImagen);
                    group.setClip(clip);

                    HBox contenedorUsuario = new HBox();
                    contenedorUsuario.setAlignment(Pos.CENTER_LEFT);
                    contenedorUsuario.getChildren().add(group);
                    contenedorUsuario.getChildren().add(new Label(usuario.getNombreUsuario()));


                    setGraphic(contenedorUsuario);
                }
            }
        };
    }
}
