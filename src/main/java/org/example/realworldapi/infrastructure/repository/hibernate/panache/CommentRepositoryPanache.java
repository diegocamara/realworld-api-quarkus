package org.example.realworldapi.infrastructure.repository.hibernate.panache;

import static io.quarkus.panache.common.Parameters.with;

import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.example.realworldapi.domain.model.article.Article;
import org.example.realworldapi.domain.model.comment.Comment;
import org.example.realworldapi.domain.model.comment.CommentRepository;
import org.example.realworldapi.infrastructure.repository.hibernate.entity.CommentEntity;
import org.example.realworldapi.infrastructure.repository.hibernate.entity.EntityUtils;

@ApplicationScoped
@AllArgsConstructor
public class CommentRepositoryPanache extends AbstractPanacheRepository<CommentEntity, UUID>
    implements CommentRepository {

  private final EntityUtils entityUtils;

  @Override
  public void save(Comment comment) {
    final var authorEntity = findUserEntityById(comment.getAuthor().getId());
    final var articleEntity = findArticleEntityById(comment.getArticle().getId());
    persist(new CommentEntity(authorEntity, articleEntity, comment));
  }

  @Override
  public Optional<Comment> findByIdAndAuthor(UUID commentId, UUID authorId) {
    return find(
            "id = :commentId and author.id = :authorId",
            with("commentId", commentId).and("authorId", authorId))
        .firstResultOptional()
        .map(entityUtils::comment);
  }

  @Override
  public void delete(Comment comment) {
    final var commentEntity = findCommentEntityById(comment.getId());
    delete(commentEntity);
  }

  @Override
  public List<Comment> findCommentsByArticle(Article article) {
    final var commentsEntity =
        find("article.id = :articleId", Parameters.with("articleId", article.getId())).list();
    return commentsEntity.stream().map(entityUtils::comment).collect(Collectors.toList());
  }
}
