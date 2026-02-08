package com.ens.common.validation;

import com.ens.common.annotation.Validation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Array;
import java.util.Collection;
import java.math.BigDecimal;
import java.util.Locale;
import java.util.Map;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.multipart.MultipartFile;

public class ValidationValidator implements ConstraintValidator<Validation, Object> {
    private Validation annotation;
    private final MessageSource messageSource;

    public ValidationValidator(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public void initialize(Validation annotation) {
        this.annotation = annotation;
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            if (annotation.required()) {
                return fail(context, com.ens.common.i18n.LabelKey.VALIDATION_REQUIRED);
            }
            return true;
        }

        if (value instanceof String str) {
            if (annotation.required() && str.trim().isEmpty()) {
                return fail(context, com.ens.common.i18n.LabelKey.VALIDATION_NOT_BLANK);
            }
            if (annotation.minLength() >= 0 && str.length() < annotation.minLength()) {
                return fail(context, com.ens.common.i18n.LabelKey.VALIDATION_SIZE);
            }
            if (annotation.maxLength() >= 0 && str.length() > annotation.maxLength()) {
                return fail(context, com.ens.common.i18n.LabelKey.VALIDATION_SIZE);
            }
            if (!annotation.regex().isEmpty() && !str.matches(annotation.regex())) {
                return fail(context, com.ens.common.i18n.LabelKey.VALIDATION_INVALID);
            }
            return true;
        }

        if (value instanceof Number num) {
            BigDecimal val;
            try {
                val = new BigDecimal(num.toString());
            } catch (NumberFormatException ex) {
                return fail(context, com.ens.common.i18n.LabelKey.VALIDATION_INVALID);
            }
            BigDecimal min = BigDecimal.valueOf(annotation.min());
            BigDecimal max = BigDecimal.valueOf(annotation.max());
            if (val.compareTo(min) < 0 || val.compareTo(max) > 0) {
                return fail(context, com.ens.common.i18n.LabelKey.VALIDATION_INVALID);
            }
            return true;
        }

        if (value instanceof Collection<?> collection) {
            if (annotation.required() && collection.isEmpty()) {
                return fail(context, com.ens.common.i18n.LabelKey.VALIDATION_REQUIRED);
            }
            return true;
        }

        if (value instanceof Map<?, ?> map) {
            if (annotation.required() && map.isEmpty()) {
                return fail(context, com.ens.common.i18n.LabelKey.VALIDATION_REQUIRED);
            }
            return true;
        }

        if (value.getClass().isArray()) {
            int length = Array.getLength(value);
            if (annotation.required() && length == 0) {
                return fail(context, com.ens.common.i18n.LabelKey.VALIDATION_REQUIRED);
            }
            return true;
        }

        if (value instanceof MultipartFile file) {
            if (annotation.required() && file.isEmpty()) {
                return fail(context, com.ens.common.i18n.LabelKey.VALIDATION_REQUIRED);
            }
            if (annotation.maxSize() > 0) {
                long maxBytes = (long) (annotation.maxSize() * 1024 * 1024);
                if (file.getSize() > maxBytes) {
                    return fail(context, com.ens.common.i18n.LabelKey.VALIDATION_INVALID);
                }
            }
            return true;
        }

        return true;
    }

    private boolean fail(ConstraintValidatorContext context, String key) {
        String message = annotation.message();
        if (message == null || message.isBlank()) {
            message = "{" + key + "}";
        }
        String resolved = resolveMessage(message, key);
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(resolved).addConstraintViolation();
        return false;
    }

    private String resolveMessage(String message, String defaultKey) {
        Locale locale = LocaleContextHolder.getLocale();
        String labelKey = annotation.label();
        String labelText = labelKey;
        if (messageSource != null && labelKey != null && !labelKey.isBlank()) {
            labelText = messageSource.getMessage(labelKey, null, labelKey, locale);
        }

        String key = defaultKey;
        if (message.startsWith("{") && message.endsWith("}")) {
            key = message.substring(1, message.length() - 1);
        }

        if (messageSource == null) {
            return message.replace("{label}", labelText);
        }
        String template = messageSource.getMessage(key, null, key, locale);
        return template.replace("{label}", labelText);
    }
}
