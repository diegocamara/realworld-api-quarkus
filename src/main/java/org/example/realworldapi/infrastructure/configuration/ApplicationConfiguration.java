package org.example.realworldapi.infrastructure.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.slugify.Slugify;
import org.example.realworldapi.infrastructure.web.qualifiers.NoWrapRootValueObjectMapper;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

@Dependent
public class ApplicationConfiguration {

  @Produces
  @Singleton
  public Slugify slugify() {
    return new Slugify();
  }

  @Singleton
  @Produces
  public ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
    objectMapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);
    objectMapper.registerModule(new JavaTimeModule());
    return objectMapper;
  }

  @Singleton
  @Produces
  @NoWrapRootValueObjectMapper
  public ObjectMapper noWrapRootValueObjectMapper() {
    return new ObjectMapper().registerModule(new JavaTimeModule());
  }
}
