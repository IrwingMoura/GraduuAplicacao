package graduuaplicacao.graduuaplicacao.Model;

public class EventoCurtido {

    private String urlPerfil;
    private String titulo;
    private String data;
    private String hora;

    public EventoCurtido() {

    }

    public EventoCurtido(String urlPerfil, String titulo, String data, String hora) {
        this.urlPerfil = urlPerfil;
        this.titulo = titulo;
        this.data = data;
        this.hora = hora;
    }

    public String getUrlPerfil() {
        return urlPerfil;
    }

    public void setUrlPerfil(String urlPerfil) {
        this.urlPerfil = urlPerfil;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
