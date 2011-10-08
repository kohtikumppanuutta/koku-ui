package fi.koku.kks.model;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class Version implements Validator {

  private String name;
  private boolean clear;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isClear() {
    return clear;
  }

  public boolean getClear() {
    return clear;
  }

  public void setClear(boolean clear) {
    this.clear = clear;
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return Version.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotEmpty.creation.name");
  }

}
