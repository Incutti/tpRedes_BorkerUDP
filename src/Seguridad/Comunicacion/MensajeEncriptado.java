package Seguridad.Comunicacion;

import java.io.Serializable;
import java.util.Base64;

public class MensajeEncriptado implements Serializable {
    private String mensajeHasheadoPrivada;
    private String mensajeEncriptadoPublica;

    public MensajeEncriptado(String mensajeHasheadoPrivada, String mensajeEncriptadoPublica) {
        this.mensajeHasheadoPrivada = mensajeHasheadoPrivada;
        this.mensajeEncriptadoPublica = mensajeEncriptadoPublica;
    }

    public String getMensajeHasheadoPrivada() {
        return mensajeHasheadoPrivada;
    }

    public void setMensajeHasheadoPrivada(String mensajeHasheadoPrivada) {
        this.mensajeHasheadoPrivada = mensajeHasheadoPrivada;
    }

    public String getMensajeEncriptadoPublica() {
        return mensajeEncriptadoPublica;
    }

    public void setMensajeEncriptadoPublica(String mensajeEncriptadoPublica) {
        this.mensajeEncriptadoPublica = mensajeEncriptadoPublica;
    }

    public static byte[] reconvertirBuffer(String mensaje){
        byte[] decodedBytes = Base64.getDecoder().decode(mensaje);
        return decodedBytes;
    }
}
