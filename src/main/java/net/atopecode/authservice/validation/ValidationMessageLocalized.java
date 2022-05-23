package net.atopecode.authservice.validation;

import java.util.List;

import net.atopecode.authservice.localization.messagelocalized.MessageLocalized;

public class ValidationMessageLocalized {
	
	protected ValidationMessageLocalized() {
		//Empty Constructor.
	}

    public static MessageLocalized forNotNull(String fieldName) {
        return new MessageLocalized(ValidationMessagesCode.NOT_NULL, fieldName);
    }

    public static MessageLocalized forMustToBeNull(String fieldName) {
        return new MessageLocalized(ValidationMessagesCode.MUST_TO_BE_NULL, fieldName);
    }

    public static MessageLocalized forNotEmpty(String fieldName) {
        return new MessageLocalized(ValidationMessagesCode.NOT_EMPTY, fieldName);
    }

    public static MessageLocalized forMustToBeEmpty(String fieldName) {
        return new MessageLocalized(ValidationMessagesCode.MUST_TO_BE_EMPTY, fieldName);
    }

    public static MessageLocalized forNotEmptyCollection(String fieldName) {
        return new MessageLocalized(ValidationMessagesCode.NOT_EMPTY_COLLECTION, fieldName);
    }

    public static MessageLocalized forMustToBeEmptyCollection(String fieldName) {
        return new MessageLocalized(ValidationMessagesCode.MUST_TO_BE_EMPTY_COLLECTION, fieldName);
    }

    public static MessageLocalized forMaxLength(String fieldName, String fieldValue, int maxLength) {
        return new MessageLocalized(ValidationMessagesCode.MAX_LENGTH, fieldName, fieldValue, maxLength);
    }

    public static MessageLocalized forMinLength(String fieldName, String fieldValue, int minLength) {
        return new MessageLocalized(ValidationMessagesCode.MIN_LENGTH, fieldName, fieldValue, minLength);
    }

    public static MessageLocalized forMaxValue(String fieldName, String fieldValue, String maxValue) {
        return new MessageLocalized(ValidationMessagesCode.MAX_VALUE, fieldName, fieldValue, maxValue);
    }

    public static MessageLocalized forMinValue(String fieldName, String fieldValue, String minValue) {
        return new MessageLocalized(ValidationMessagesCode.MIN_VALUE, fieldName, fieldValue, minValue);
    }

    public static MessageLocalized forEqualsValue(String fieldName, String fieldValue, String equalsValue) {
        return new MessageLocalized(ValidationMessagesCode.EQUALS_VALUE, fieldName, fieldValue, equalsValue);
    }

    public static MessageLocalized forNotEqualsValue(String fieldName, String fieldValue, String equalsValue) {
        return new MessageLocalized(ValidationMessagesCode.NOT_EQUALS_VALUE, fieldName, fieldValue, equalsValue);
    }

    public static MessageLocalized forValueIn(String fieldName, String fieldValue, List<String> values) {
        return new MessageLocalized(ValidationMessagesCode.VALUE_IN, fieldName, fieldValue, values);
    }

    public static MessageLocalized forValueNotIn(String fieldName, String fieldValue, List<String> values) {
        return new MessageLocalized(ValidationMessagesCode.VALUE_NOT_IN, fieldName, fieldValue, values);
    }

    public static MessageLocalized forMustToBeTrue(String fieldName) {
        return new MessageLocalized(ValidationMessagesCode.MUST_TO_BE_TRUE, fieldName);
    }

    public static MessageLocalized forMustToBeFalse(String fieldName) {
        return new MessageLocalized(ValidationMessagesCode.MUST_TO_BE_FALSE, fieldName);
    }

    public static MessageLocalized forHasFormat(String fieldName, String fieldValue) {
        return new MessageLocalized(ValidationMessagesCode.HAS_FORMAT, fieldName, fieldValue);
    }

    public static MessageLocalized forNotNullValue(String valueName) {
        return new MessageLocalized(ValidationMessagesCode.NOT_NULL_VALUE, valueName);
    }

    public static MessageLocalized forMustToBeNullValue(String valueName) {
        return new MessageLocalized(ValidationMessagesCode.MUST_TO_BE_NULL_VALUE, valueName);
    }
}
