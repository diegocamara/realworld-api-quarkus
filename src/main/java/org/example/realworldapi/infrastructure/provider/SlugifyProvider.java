package org.example.realworldapi.infrastructure.provider;

import com.github.slugify.Slugify;
import org.example.realworldapi.domain.model.provider.SlugProvider;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SlugifyProvider implements SlugProvider {

  private Slugify slugify;

  public SlugifyProvider(Slugify slugify) {
    this.slugify = slugify;
  }

  @Override
  public String slugify(String text) {
    return slugify.slugify(text);
  }
}
