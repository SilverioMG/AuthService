package net.atopecode.authservice.validation.exceptions;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.atopecode.authservice.localization.messagelocalized.MessageLocalized;

public class ValidationBundleException extends ValidationException {

    private List<MessageLocalized> errors = new ArrayList<>();

    /**
     * Mensaje para devolver al cliente web traducido según el Locale (el locale se establece a partir de los Headers de la petición Http).
     */
    public ValidationBundleException(String loggerMessage, MessageLocalized validationBundleErrorMessage, List<MessageLocalized> errors) {
        super(loggerMessage, validationBundleErrorMessage);
        this.errors = (errors != null) ? errors : new ArrayList<>();
    }

    /**
     * Devuelve una 'Inmutable List' con el contenido de los errores de validación.
     * @return
     */
    public List<MessageLocalized> getErrors(){
        return Collections.unmodifiableList(errors);
    }

    @Override
    public String toString() {
        return "ValidationException{" +
                "validationErrorMessage=" + super.getMessageLocalized() +
                "errors=" + errors +
                '}';
    }
}
