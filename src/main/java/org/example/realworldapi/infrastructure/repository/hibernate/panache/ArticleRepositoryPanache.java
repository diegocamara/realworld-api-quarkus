package org.example.realworldapi.infrastructure.repository.hibernate.panache;

import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.*;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.example.realworldapi.domain.model.article.Article;
import org.example.realworldapi.domain.model.article.ArticleFilter;
import org.example.realworldapi.domain.model.article.ArticleRepository;
import org.example.realworldapi.domain.model.article.PageResult;
import org.example.realworldapi.infrastructure.repository.hibernate.entity.ArticleEntity;
import org.example.realworldapi.infrastructure.repository.hibernate.entity.EntityUtils;
import org.example.realworldapi.infrastructure.repository.hibernate.panache.utils.SimpleQueryBuilder;

@ApplicationScoped
@AllArgsConstructor
public class ArticleRepositoryPanache extends AbstractPanacheRepository<ArticleEntity, UUID>
    implements ArticleRepository {

  private final EntityUtils entityUtils;

  @Override
  public boolean existsBySlug(String slug) {
    return count("upper(slug)", slug.toUpperCase().trim()) > 0;
  }

  @Override
  public void save(Article article) {
    final var author = findUserEntityById(article.getAuthor().getId());
    persistAndFlush(new ArticleEntity(article, author));
  }

  @Override
  public Optional<Article> findArticleById(UUID id) {
    return findByIdOptional(id).map(entityUtils::article);
  }

  @Override
  public Optional<Article> findBySlug(String slug) {
    return find("upper(slug)", slug.toUpperCase().trim())
        .firstResultOptional()
        .map(entityUtils::article);
  }

  @Override
  public void update(Article article) {
    final var articleEntity = findArticleEntityById(article.getId());
    articleEntity.update(article);
  }

  @Override
  public Optional<Article> findByAuthorAndSlug(UUID authorId, String slug) {
    return find(
            "author.id = :authorId and upper(slug) = :slug",
            Parameters.with("authorId", authorId).and("slug", slug.toUpperCase().trim()))
        .firstResultOptional()
        .map(entityUtils::article);
  }

  @Override
  public void delete(Article article) {
    deleteById(article.getId());
  }

  @Override
  public PageResult<Article> findMostRecentArticlesByFilter(ArticleFilter articleFilter) {
    final var articlesEntity =
        find(
                "select articles from ArticleEntity as articles inner join articles.author as author inner join author.followedBy as followedBy where followedBy.user.id = :loggedUserId",
                Sort.descending("createdAt").and("updatedAt").descending(),
                Parameters.with("loggedUserId", articleFilter.getLoggedUserId()))
            .page(Page.of(articleFilter.getOffset(), articleFilter.getLimit()))
            .list();
    final var articlesResult =
        articlesEntity.stream().map(entityUtils::article).collect(Collectors.toList());
    final var total = count(articleFilter.getLoggedUserId());
    return new PageResult<>(articlesResult, total);
  }

  @Override
  public PageResult<Article> findArticlesByFilter(ArticleFilter filter) {
    Map<String, Object> params = new LinkedHashMap<>();
    SimpleQueryBuilder findArticlesQueryBuilder = new SimpleQueryBuilder();
    findArticlesQueryBuilder.addQueryStatement("select articles from ArticleEntity as articles");
    configFilterFindArticlesQueryBuilder(
        findArticlesQueryBuilder,
        filter.getTags(),
        filter.getAuthors(),
        filter.getFavorited(),
        params);
    final var articlesEntity =
        find(
                findArticlesQueryBuilder.toQueryString(),
                Sort.descending("createdAt").and("updatedAt").descending(),
                params)
            .page(Page.of(filter.getOffset(), filter.getLimit()))
            .list();
    final var articlesResult =
        articlesEntity.stream().map(entityUtils::article).collect(Collectors.toList());
    final var total = count(filter.getTags(), filter.getAuthors(), filter.getFavorited());
    return new PageResult<>(articlesResult, total);
  }

  @Override
  public long count(List<String> tags, List<String> authors, List<String> favorited) {
    Map<String, Object> params = new LinkedHashMap<>();
    SimpleQueryBuilder countArticlesQueryBuilder = new SimpleQueryBuilder();
    countArticlesQueryBuilder.addQueryStatement("from ArticleEntity as articles");
    configFilterFindArticlesQueryBuilder(
        countArticlesQueryBuilder, tags, authors, favorited, params);
    return count(countArticlesQueryBuilder.toQueryString(), params);
  }

  public long count(UUID loggedUserId) {
    return count(
        "from ArticleEntity as articles inner join articles.author as author inner join author.followedBy as followedBy where followedBy.user.id = :loggedUserId",
        Parameters.with("loggedUserId", loggedUserId));
  }

  private void configFilterFindArticlesQueryBuilder(
      SimpleQueryBuilder findArticlesQueryBuilder,
      List<String> tags,
      List<String> authors,
      List<String> favorited,
      Map<String, Object> params) {

    findArticlesQueryBuilder.updateQueryStatementConditional(
        isNotEmpty(tags),
        "inner join articles.tags as tags inner join tags.primaryKey.tag as tag",
        "upper(tag.name) in (:tags)",
        () -> params.put("tags", toUpperCase(tags)));

    findArticlesQueryBuilder.updateQueryStatementConditional(
        isNotEmpty(authors),
        "inner join articles.author as authors",
        "upper(authors.username) in (:authors)",
        () -> params.put("authors", toUpperCase(authors)));

    findArticlesQueryBuilder.updateQueryStatementConditional(
        isNotEmpty(favorited),
        "inner join articles.favorites as favorites inner join favorites.primaryKey.user as user",
        "upper(user.username) in (:favorites)",
        () -> params.put("favorites", toUpperCase(favorited)));
  }
}
