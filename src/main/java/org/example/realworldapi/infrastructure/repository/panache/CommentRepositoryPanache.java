package org.example.realworldapi.infrastructure.repository.panache;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.example.realworldapi.domain.model.entity.Comment;
import org.example.realworldapi.domain.model.repository.CommentRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

import static io.quarkus.panache.common.Parameters.with;

@ApplicationScoped
public class CommentRepositoryPanache implements PanacheRepository<Comment>, CommentRepository {

  @Override
  public Comment create(Comment comment) {
    persistAndFlush(comment);
    return comment;
  }

  @Override
  public Optional<Comment> findComment(String slug, Long commentId, Long authorId) {
    return find(
            "id = :commentId and upper(article.slug) = :slug and author.id = :authorId",
            with("commentId", commentId)
                .and("slug", slug.toUpperCase().trim())
                .and("authorId", authorId))
        .firstResultOptional();
  }

  @Override
  public void remove(Comment comment) {
    delete(comment);
  }

  @Override
  public List<Comment> findArticleComments(Long articleId) {
    return find("article.id", articleId).list();
  }
}
