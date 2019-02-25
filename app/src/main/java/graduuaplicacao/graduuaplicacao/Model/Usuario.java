package graduuaplicacao.graduuaplicacao.Model;

public class Usuario {

    private String email;
    private String senha;
    private String nome;
    private String matricula;
    private String campus;
    private String dataDeNascimento;
    private String imagemPerfil;
    private String curso;

    public Usuario() {
    }

    public Usuario(String email, String senha, String nome, String matricula, String campus, String dataDeNascimento, String imagemPerfil, String curso) {
        this.email = email;
        this.senha = senha;
        this.nome = nome;
        this.matricula = matricula;
        this.campus = campus;
        this.dataDeNascimento = dataDeNascimento;
        this.curso = curso;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String login) {
        this.email = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getDataDeNascimento() {
        return dataDeNascimento;
    }

    public void setDataDeNascimento(String dataDeNascimento) {
        this.dataDeNascimento = dataDeNascimento;
    }

    public String getImagemPerfil() {
        return imagemPerfil;
    }

    public void setImagemPerfil(String imagemPerfil) {
        this.imagemPerfil = imagemPerfil;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }
}
