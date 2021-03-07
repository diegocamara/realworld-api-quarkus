package org.example.realworldapi.infrastructure.configuration;

import org.example.realworldapi.domain.feature.*;
import org.example.realworldapi.domain.feature.impl.*;
import org.example.realworldapi.domain.model.provider.HashProvider;
import org.example.realworldapi.domain.model.user.FollowRelationshipRepository;
import org.example.realworldapi.domain.model.user.NewUserRepository;
import org.example.realworldapi.domain.model.user.UserModelBuilder;
import org.example.realworldapi.domain.validator.ModelValidator;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;
import javax.validation.Validator;

@Dependent
public class UsersConfiguration {

  @Produces
  @Singleton
  public CreateUser createUser(
      NewUserRepository userRepository, HashProvider hashProvider, UserModelBuilder userBuilder) {
    return new CreateUserImpl(userRepository, hashProvider, userBuilder);
  }

  @Produces
  @Singleton
  public UpdateUser updateUser(
      FindUserById findUserById, NewUserRepository userRepository, ModelValidator modelValidator) {
    return new UpdateUserImpl(findUserById, userRepository, modelValidator);
  }

  @Produces
  @Singleton
  public FindUserById findUserById(NewUserRepository userRepository) {
    return new FindUserByIdImpl(userRepository);
  }

  @Produces
  @Singleton
  public LoginUser loginUser(NewUserRepository userRepository, HashProvider hashProvider) {
    return new LoginUserImpl(userRepository, hashProvider);
  }

  @Produces
  @Singleton
  public FindUserByUsername findUserByUsername(NewUserRepository userRepository) {
    return new FindUserByUsernameImpl(userRepository);
  }

  @Produces
  @Singleton
  public IsFollowingUser isFollowingUser(FollowRelationshipRepository usersFollowedRepository) {
    return new IsFollowingUserImpl(usersFollowedRepository);
  }

  @Produces
  @Singleton
  public FollowUserByUsername followUserByUsername(
      FindUserById findUserById,
      FindUserByUsername findUserByUsername,
      FollowRelationshipRepository followRelationshipRepository) {
    return new FollowUserByUsernameImpl(
        findUserById, findUserByUsername, followRelationshipRepository);
  }

  @Produces
  @Singleton
  public UnfollowUserByUsername unfollowUserByUsername(
      FindUserById findUserById,
      FindUserByUsername findUserByUsername,
      FollowRelationshipRepository followRelationshipRepository) {
    return new UnfollowUserByUsernameImpl(
        findUserById, findUserByUsername, followRelationshipRepository);
  }

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
