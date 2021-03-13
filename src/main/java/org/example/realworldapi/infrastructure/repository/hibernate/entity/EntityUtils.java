package org.example.realworldapi.infrastructure.repository.hibernate.entity;

import lombok.AllArgsConstructor;
import org.example.realworldapi.domain.model.article.Article;
import org.example.realworldapi.domain.model.article.ArticleModelBuilder;
import org.example.realworldapi.domain.model.tag.Tag;
import org.example.realworldapi.domain.model.tag.TagBuilder;
import org.example.realworldapi.domain.model.user.User;
import org.example.realworldapi.domain.model.user.UserModelBuilder;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@AllArgsConstructor
public class EntityUtils {
  private final UserModelBuilder userBuilder;
  private final TagBuilder tagBuilder;
  private final ArticleModelBuilder articleBuilder;

  public User user(UserEntity userEntity) {
    final var id = userEntity.getId();
    final var username = userEntity.getUsername();
    final var bio = userEntity.getBio();
    final var image = userEntity.getImage();
    final var password = userEntity.getPassword();
    final var email = userEntity.getEmail();
    return userBuilder.build(id, username, bio, image, password, email);
  }

  public Tag tag(TagEntity tagEntity) {
    return tagBuilder.build(tagEntity.getId(), tagEntity.getName());
  }

  public Tag tag(TagRelationshipEntity tagRelationshipEntity) {
    return tag(tagRelationshipEntity.getPrimaryKey().getTag());
  }

  public Article article(ArticleEntity articleEntity) {
    return articleBuilder.build(
        articleEntity.getId(),
        articleEntity.getSlug(),
        articleEntity.getTitle(),
        articleEntity.getDescription(),
        articleEntity.getBody(),
        articleEntity.getCreatedAt(),
        articleEntity.getUpdatedAt(),
        user(articleEntity.getAuthor()));
  }
}
