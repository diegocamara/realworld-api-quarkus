package org.example.realworldapi.infrastructure.repository.hibernate.panache;

import org.example.realworldapi.domain.model.comment.Comment;
import org.example.realworldapi.domain.model.comment.NewCommentRepository;
import org.example.realworldapi.infrastructure.repository.hibernate.entity.CommentEntity;

import javax.enterprise.context.ApplicationScoped;
import java.util.UUID;

@ApplicationScoped
public class NewCommentRepositoryPanache extends AbstractPanacheRepository<CommentEntity, UUID>
    implements NewCommentRepository {

  @Override
  public void save(Comment comment) {
    final var authorEntity = findUserEntityById(comment.getAuthor().getId());
    final var articleEntity = findArticleEntityById(comment.getArticle().getId());
    persist(new CommentEntity(authorEntity, articleEntity, comment));
  }
}
