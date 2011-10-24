package fi.koku.kks.model;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Class for holding information for collection creation
 * 
 * @author tuomape
 * 
 */
public class Creation implements Validator {

  private String name;
  private String field;

  public String getField() {
    return field;
  }

  public void setField(String field) {
    this.field = field;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return Creation.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotEmpty.creation.name");
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "field", "NotEmpty.creation.field");
  }

}
