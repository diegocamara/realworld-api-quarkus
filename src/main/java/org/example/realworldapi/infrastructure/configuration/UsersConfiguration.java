package org.example.realworldapi.infrastructure.configuration;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;
import jakarta.validation.Validator;
import org.example.realworldapi.domain.feature.*;
import org.example.realworldapi.domain.feature.impl.*;
import org.example.realworldapi.domain.model.provider.HashProvider;
import org.example.realworldapi.domain.model.user.FollowRelationshipRepository;
import org.example.realworldapi.domain.model.user.UserModelBuilder;
import org.example.realworldapi.domain.model.user.UserRepository;
import org.example.realworldapi.domain.validator.ModelValidator;

@Dependent
public class UsersConfiguration {

  @Produces
  @Singleton
  public CreateUser createUser(
      UserRepository userRepository, HashProvider hashProvider, UserModelBuilder userBuilder) {
    return new CreateUserImpl(userRepository, hashProvider, userBuilder);
  }

  @Produces
  @Singleton
  public UpdateUser updateUser(
      FindUserById findUserById, UserRepository userRepository, ModelValidator modelValidator) {
    return new UpdateUserImpl(findUserById, userRepository, modelValidator);
  }

  @Produces
  @Singleton
  public FindUserById findUserById(UserRepository userRepository) {
    return new FindUserByIdImpl(userRepository);
  }

  @Produces
  @Singleton
  public LoginUser loginUser(UserRepository userRepository, HashProvider hashProvider) {
    return new LoginUserImpl(userRepository, hashProvider);
  }

  @Produces
  @Singleton
  public FindUserByUsername findUserByUsername(UserRepository userRepository) {
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
