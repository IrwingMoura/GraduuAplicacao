package graduuaplicacao.graduuaplicacao.Util;

import java.util.GregorianCalendar;
import java.util.StringTokenizer;

public class Validador {
    public boolean validarData(String data) {
        String dia;
        String mes;
        String ano;
        StringTokenizer token = new StringTokenizer(data, "/");
        try {
            dia = token.nextToken();
            mes = token.nextToken();
            ano = token.nextToken();
            if (dia.length() < 2 || dia.length() > 2)
                return false;
            if (mes.length() < 2 || mes.length() > 2)
                return false;
            if (ano.length() < 4 || dia.length() > 4)
                return false;
            int intDia = Integer.parseInt(dia);
            int intMes = Integer.parseInt(mes);
            int intAno = Integer.parseInt(ano);
            if (intMes < 1 || intMes > 12)
                return false;
            if (intMes == 1 || intMes == 3 || intMes == 5 || intMes == 7
                    || intMes == 8 || intMes == 12) {
                if (intDia < 1 || intDia > 31)
                    return false;
            } else if (intMes == 4 || intMes == 6 || intMes == 9
                    || intMes == 10 || intMes == 11) {
                if (intDia < 1 || intDia > 30)
                    return false;
            } else if (!new GregorianCalendar().isLeapYear(intAno)) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}