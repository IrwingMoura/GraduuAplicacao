package graduuaplicacao.graduuaplicacao.Model;

public class ListaHorasPorID {

    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public ListaHorasPorID(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public ListaHorasPorID(){

    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
