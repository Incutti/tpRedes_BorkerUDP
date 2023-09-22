package Seguridad.Comunicacion;

import java.io.Serializable;
import java.util.Base64;

public class MensajeEncriptadoSimetrico implements Serializable {
    private String mensajeHasheadoPrivada;
    private String mensajeEncriptadoClave;

    public MensajeEncriptadoSimetrico(String mensajeHasheadoPrivada, String mensajeEncriptadoPublica) {
        this.mensajeHasheadoPrivada = mensajeHasheadoPrivada;
        this.mensajeEncriptadoClave = mensajeEncriptadoPublica;
    }

    public String getMensajeHasheadoPrivada() {
        return mensajeHasheadoPrivada;
    }

    public void setMensajeHasheadoPrivada(String mensajeHasheadoPrivada) {
        this.mensajeHasheadoPrivada = mensajeHasheadoPrivada;
    }

    public String getMensajeEncriptadoClave() {
        return mensajeEncriptadoClave;
    }

    public void setMensajeEncriptadoClave(String mensajeEncriptadoClave) {
        this.mensajeEncriptadoClave = mensajeEncriptadoClave;
    }

    public static byte[] reconvertirBuffer(String mensaje){
        byte[] decodedBytes=new byte[4096];
        decodedBytes = Base64.getDecoder().decode(mensaje);
        return decodedBytes;
    }
}

