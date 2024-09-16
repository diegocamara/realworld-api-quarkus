package org.example.realworldapi.infrastructure.web.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import org.example.realworldapi.infrastructure.web.validation.constraint.AtLeastOneFieldMustBeNotNull;

public class AtLeastOneFieldMustBeNotNullValidator
    implements ConstraintValidator<AtLeastOneFieldMustBeNotNull, Object> {

  private String[] fieldNames;

  public void initialize(AtLeastOneFieldMustBeNotNull constraintAnnotation) {
    fieldNames = constraintAnnotation.fieldNames();
  }

  public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {

    Class<?> clazz = object.getClass();

    configFieldNamesIfEmpty(clazz);

    for (String propertyName : fieldNames) {
      Field field = getDeclaredField(clazz, propertyName);
      if (getValue(field, object) != null) {
        return true;
      }
    }

    return false;
  }

  private void configFieldNamesIfEmpty(Class<?> clazz) {

    if (fieldNames.length == 0) {
      fieldNames = getInstanceDeclaredFieldNames(clazz);
    }
  }

  private String[] getInstanceDeclaredFieldNames(Class<?> clazz) {

    Field[] fields = clazz.getDeclaredFields();

    String[] resultNames = new String[fields.length];

    for (int fieldIndex = 0; fieldIndex < fields.length; fieldIndex++) {
      resultNames[fieldIndex] = fields[fieldIndex].getName();
    }

    return resultNames;
  }

  private Object getValue(Field field, Object instance) {
    Object value;
    try {
      value = field.get(instance);
    } catch (IllegalAccessException ex) {
      throw new RuntimeException(ex);
    }
    return value;
  }

  private Field getDeclaredField(Class<?> clazz, String name) {
    Field field;
    try {
      field = clazz.getDeclaredField(name);
      field.setAccessible(true);
    } catch (NoSuchFieldException ex) {
      throw new RuntimeException(ex);
    }
    return field;
  }
}
