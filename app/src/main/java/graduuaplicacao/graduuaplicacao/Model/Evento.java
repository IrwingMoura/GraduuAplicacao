package graduuaplicacao.graduuaplicacao.Model;

import android.widget.ImageView;

public class Evento {

    private String nome;
    private String horaInicio;
    private String horaFim;
    private String data;
    private String descricao;
    private String apresentador;
    private String frequencia;
    private String local;
    private String categoria;
    private Boolean curtiu;
    private String urlFotoUsuarioCard;
    private Integer codCategoria;
    private String idUsuarioLogado;
    private boolean isChecked;
    private String deepLink;
    private long numShares;
    private String id;
    private String horasComplementares;


    public Evento(String nome, String horaInicio, String horaFim, String data, String descricao, String apresentador, String frequencia, String local, String categoria, String idUsuarioLogado, boolean isChecked, String deepLink, long numShares, Boolean curti, String urlFotoUsuarioCard, Integer codCategoria, String id, String horasComplementares) {
        this.nome = nome;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.data = data;
        this.descricao = descricao;
        this.apresentador = apresentador;
        this.frequencia = frequencia;
        this.local = local;
        this.categoria = categoria;
        this.idUsuarioLogado = idUsuarioLogado;
        this.isChecked = isChecked;
        this.deepLink = deepLink;
        this.numShares = numShares;
        this.curtiu = curtiu;
        this.urlFotoUsuarioCard = urlFotoUsuarioCard;
        this.codCategoria = codCategoria;
        this.id = id;
        this.horasComplementares = horasComplementares;

    }

    public Evento() {
    }

    public String getIdUsuarioLogado() {
        return idUsuarioLogado;
    }

    public String getDeepLink() {
        return deepLink;
    }

    public void setDeepLink(String deepLink) {
        this.deepLink = deepLink;
    }

    public long getNumShares() {
        return numShares;
    }

    public void setNumShares(long numShares) {
        this.numShares = numShares;
    }

    public void setIdUsuarioLogado(String idUsuarioLogado) {
        this.idUsuarioLogado = idUsuarioLogado;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(String horaFim) {
        this.horaFim = horaFim;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getApresentador() {
        return apresentador;
    }

    public void setApresentador(String apresentador) {
        this.apresentador = apresentador;
    }

    public String getFrequencia() {
        return frequencia;
    }

    public void setFrequencia(String frequencia) {
        this.frequencia = frequencia;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public Boolean getCurtiu() {
        return curtiu;
    }

    public void setCurtiu(Boolean curtiu) {
        this.curtiu = curtiu;
    }

    public String getUrlFotoUsuarioCard() {
        return urlFotoUsuarioCard;
    }

    public void setUrlFotoUsuarioCard(String urlFotoUsuarioCard) {
        this.urlFotoUsuarioCard = urlFotoUsuarioCard;
    }

    public Integer getCodCategoria() {
        return codCategoria;
    }

    public void setCodCategoria(Integer codCategoria) {
        this.codCategoria = codCategoria;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHorasComplementares() {
        return horasComplementares;
    }

    public void setHorasComplementares(String horasComplementares) {
        this.horasComplementares = horasComplementares;
    }
}
