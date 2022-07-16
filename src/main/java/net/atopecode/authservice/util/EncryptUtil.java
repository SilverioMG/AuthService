package net.atopecode.authservice.util;
import org.apache.commons.codec.binary.Hex;
import org.springframework.util.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//Esta clase codifica cadenas de texto utilizando algoritmos tipo Hash (un único sentido) como 'SHA-512', 'SHA-1' o 'MD5'.
public class EncryptUtil {

    private MessageDigest messageDigest;

    public EncryptUtil(String algorithm) throws NoSuchAlgorithmException {
        messageDigest = MessageDigest.getInstance(algorithm);
    }

    /**
     * Este método codifica el 'String' recibido como parámetro y devuelve su codificación según el algoritmo escogido y
     * en formato Hexadecimal.
     * @param value
     * @return
     */
    public String encrypt(String value){
        String result = null;
        if(StringUtils.isEmpty(value)){
            return result;
        }

        messageDigest.update(value.getBytes());
        byte[] digest = messageDigest.digest();
        result = Hex.encodeHexString(digest);

        return result;
    }

    public static class Algorithm{
        public final static String SHA_512 = "SHA-512";
        public final static String SHA_1 = "SHA-1";
        public final static String MD5 = "MD5";
    }

    public static EncryptUtil SHA512EncrypUtil(){
        EncryptUtil encryptUtil = null;
        try{
            return new EncryptUtil(Algorithm.SHA_512);
        }
        catch (Exception ex){
            return encryptUtil;
        }
    }

    public static EncryptUtil SHA1EncrypUtil(){
        EncryptUtil encryptUtil = null;
        try{
            return new EncryptUtil(Algorithm.SHA_1);
        }
        catch (Exception ex){
            return encryptUtil;
        }
    }

    public static EncryptUtil MD5EncrypUtil(){
        EncryptUtil encryptUtil = null;
        try{
            return new EncryptUtil(Algorithm.MD5);
        }
        catch (Exception ex){
            return encryptUtil;
        }
    }

}
