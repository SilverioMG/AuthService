package net.atopecode.authservice.util;

import java.text.Normalizer;
import java.util.Map;

//Esta clase provee métodos para devolver el valor normalizado de Strings (quitando tildes y caracteres especiales).
public class NormalizeString {

    public static String withoutAccents(String value){
        if(value != null){
            value = Normalizer.normalize(value, Normalizer.Form.NFD);
            value = value.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        }

        return value;
    }

    public static String withoutWhiteSpaces(String value){
        if(value != null){
            value = value.replaceAll("\\s", "");
        }

        return value;
    }

    public static String withoutSpecialChars(String value){
        if(value != null){
            //Todos los caracteres están escapados con '\\' para al expresión regular.
            String replace = "\\ª\\º\\!\"\\·\\$\\%\\&\\/\\(\\)\\=\\?\\¿\\Ç\\|\\@\\#\\~\\½\\¬\\{\\[\\]\\}\\*\\/\\-\\+\\^\\-\\_\\.\\:\\,\\;\\<\\>";
            String regexReplace = "[" + replace + "]";
            value = value.replaceAll(regexReplace, "");
        }

        return value;
    }

    /**
     * Este método normaliza el 'String' recibido como parámetro convirtiendo sus caracteres en minúsculas, quitando espacios en blanco,
     * quitando acentos/tildes y caracteres especiales.
     * @param value
     * @return
     */
    public static String normalize(String value){
        if(value == null){
            return value;
        }

        value = withoutWhiteSpaces(value);
        value = withoutSpecialChars(value);
        value = withoutAccents(value);
        value = value.toLowerCase();
        return value;
    }

    public static String replace(String value, Map<Character, Character> mapReplaceTo){
        if(mapReplaceTo == null){
            return value;
        }
        if(value == null){
            return value;
        }

        char[] chars = value.toCharArray();
        Character replaceTo;
        for(int ind = 0; ind < chars.length; ind++){
            replaceTo = mapReplaceTo.get(chars[ind]);
            if(replaceTo != null){
                chars[ind] = replaceTo;
            }
        }

        return chars.toString();
    }
}
