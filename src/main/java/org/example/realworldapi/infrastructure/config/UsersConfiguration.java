package org.example.realworldapi.infrastructure.config;

import lombok.AllArgsConstructor;
import org.example.realworldapi.domain.model.user.UserModelBuilder;
import org.example.realworldapi.domain.validator.ModelValidator;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;
import javax.validation.Validator;

@Dependent
@AllArgsConstructor
public class UsersConfiguration {

  @Produces
  @Singleton
  public UserModelBuilder userModelBuilder(ModelValidator modelValidator) {
    return new UserModelBuilder(modelValidator);
  }

  @Produces
  @Singleton
  public ModelValidator modelValidator(Validator validator) {
    return new ModelValidator(validator);
  }
}
