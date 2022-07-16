package net.atopecode.authservice.config.security.utils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import net.atopecode.authservice.util.EncryptUtil;

import java.security.NoSuchAlgorithmException;

public class EncryptPasswordEncoder implements PasswordEncoder {
    public static final Logger LOGGER = LoggerFactory.getLogger(EncryptPasswordEncoder.class);

    //Este objeto se utiliza para encriptar el password que proviene cliente web en la authenticación con el mismo algoritmo que se guarda el password del 'Usuario' en la B.D.
    private EncryptUtil encryptUtil;

    //Indica si el password que viene desde el 'ClienteWeb' para Authenticarse está encryptado (igual que el password de los 'Usuarios' en la B.D.)
    private boolean webClientPasswordEncoded = false;

    public EncryptPasswordEncoder(boolean webClientPasswordEncoded) {
        this.webClientPasswordEncoded = webClientPasswordEncoded;
        this.encryptUtil = EncryptUtil.SHA1EncrypUtil();
    }

    public EncryptPasswordEncoder(String algorithm) throws NoSuchAlgorithmException {
        if(StringUtils.isEmpty(algorithm)){
            algorithm = EncryptUtil.Algorithm.SHA_1;
        }
        this.encryptUtil = new EncryptUtil(algorithm);
    }

    @Override
    public String encode(CharSequence rawPassword) {
        if((rawPassword == null) || (rawPassword.length() == 0)){
            return null;
        }
        String password = rawPassword.toString();
        String encodedPassword = encryptUtil.encrypt(password);
        return encodedPassword;
    }

    /**
     * Este método comprueba si el password recibido desde el 'ClienteWeb' (rawPassword) coincide con el del 'Usuario' guardado en la B.D.
     * cuando se realiza la 'Authenticacion' (para obtener el Token (JWT)).
     * El campo 'webClientPasswordEncoded' indica si el password del cliente web viene ya encryptado o no.
     * @param rawPassword Password que proviene del cliente web para authenticarse.
     * @param encodedPassword Password del 'Usuario' en la B.D.
     * @return
     */
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (encodedPassword != null && encodedPassword.length() != 0) {
            String rawPasswordString = rawPassword.toString();
            String encodedRawPassword = (!webClientPasswordEncoded) ? encryptUtil.encrypt(rawPasswordString) : rawPasswordString;
            if(StringUtils.equals(encodedRawPassword, encodedPassword)){
                return true;
            }
            return false;

        } else {
            LOGGER.warn("Empty encoded password");
            return false;
        }
    }
}

