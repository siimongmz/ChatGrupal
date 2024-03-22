package com.util;

import lombok.Getter;

import java.io.*;
import java.util.Base64;

public class Usuario implements Serializable {
    @Getter
    private final String nombreUsuario;
    @Getter
    private String base64Perfil;

    public Usuario(String nombreUsuario, String base64Perfil) {
        this.nombreUsuario = nombreUsuario;
        this.base64Perfil = base64Perfil;
    }



    //https://github.com/loizenai/image-to-base-64-java-8


}
