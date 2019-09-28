package org.example.realworldapi.domain.config;

import com.github.slugify.Slugify;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

@ApplicationScoped
public class DomainConfig {

  @Singleton
  @Produces
  public Slugify slugify() {
    return new Slugify();
  }
}
