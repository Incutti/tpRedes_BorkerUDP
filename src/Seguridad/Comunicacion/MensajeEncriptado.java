package Seguridad.Comunicacion;

public class MensajeEncriptado {
    private byte[] mensajeHasheadoPrivada;
    private byte[] mensajeEncriptadoPublica;

    private static String separador="separador";



    public MensajeEncriptado(byte[] mensajeHasheadoPrivada, byte[] mensajeEncriptadoPublica) {
        this.mensajeHasheadoPrivada = mensajeHasheadoPrivada;
        this.mensajeEncriptadoPublica = mensajeEncriptadoPublica;
    }

    public byte[] getMensajeHasheadoPrivada() {
        return mensajeHasheadoPrivada;
    }

    public void setMensajeHasheadoPrivada(byte[] mensajeHasheadoPrivada) {
        this.mensajeHasheadoPrivada = mensajeHasheadoPrivada;
    }

    public byte[] getMensajeEncriptadoPublica() {
        return mensajeEncriptadoPublica;
    }

    public void setMensajeEncriptadoPublica(byte[] mensajeEncriptadoPublica) {
        this.mensajeEncriptadoPublica = mensajeEncriptadoPublica;
    }

    public static String getSeparador() {
        return separador;
    }

    public static void setSeparador(String separador) {
        MensajeEncriptado.separador = separador;
    }
}
