package org.example.realworldapi.infrastructure.repository.panache;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import org.example.realworldapi.domain.model.entity.Article;
import org.example.realworldapi.domain.model.repository.ArticleRepository;
import org.example.realworldapi.infrastructure.utils.SimpleQueryBuilder;

import javax.enterprise.context.ApplicationScoped;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class ArticleRepositoryPanache implements PanacheRepository<Article>, ArticleRepository {

  @Override
  public List<Article> findArticles(
      int offset, int limit, List<String> tags, List<String> authors, List<String> favorited) {
    Map<String, Object> params = new LinkedHashMap<>();
    SimpleQueryBuilder findArticlesQueryBuilder = new SimpleQueryBuilder();
    findArticlesQueryBuilder.addQueryStatement("select articles from Article as articles");
    configFilterFindArticlesQueryBuilder(
        findArticlesQueryBuilder, tags, authors, favorited, params);
    return find(
            findArticlesQueryBuilder.toQueryString(),
            Sort.descending("createdAt").and("updatedAt").descending(),
            params)
        .page(Page.of(offset, limit))
        .list();
  }

  @Override
  public Article create(Article article) {
    persistAndFlush(article);
    return article;
  }

  @Override
  public boolean existsBySlug(String slug) {
    return count("upper(slug)", slug.toUpperCase().trim()) > 0;
  }

  @Override
  public Optional<Article> findBySlug(String slug) {
    return find("upper(slug)", slug.toUpperCase().trim()).firstResultOptional();
  }

  @Override
  public void remove(Article article) {
    delete(article);
  }

  @Override
  public Optional<Article> findByIdAndSlug(Long authorId, String slug) {
    return find(
            "author.id = :authorId and upper(slug) = :slug",
            Parameters.with("authorId", authorId).and("slug", slug.toUpperCase().trim()))
        .firstResultOptional();
  }

  @Override
  public List<Article> findMostRecentArticles(Long loggedUserId, int offset, int limit) {
    return find(
            "select articles from Article as articles inner join articles.author as author inner join author.followedBy as followedBy where followedBy.user.id = :loggedUserId",
            Sort.descending("createdAt").and("updatedAt").descending(),
            Parameters.with("loggedUserId", loggedUserId))
        .page(Page.of(offset, limit))
        .list();
  }

  @Override
  public long count(List<String> tags, List<String> authors, List<String> favorited) {
    Map<String, Object> params = new LinkedHashMap<>();
    SimpleQueryBuilder countArticlesQueryBuilder = new SimpleQueryBuilder();
    countArticlesQueryBuilder.addQueryStatement("from Article as articles");
    configFilterFindArticlesQueryBuilder(
        countArticlesQueryBuilder, tags, authors, favorited, params);
    return count(countArticlesQueryBuilder.toQueryString(), params);
  }

  @Override
  public long count(Long loggedUserId) {
    return count(
        "from Article as articles inner join articles.author as author inner join author.followedBy as followedBy where followedBy.user.id = :loggedUserId",
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

  private boolean isNotEmpty(List<?> list) {
    return list != null && !list.isEmpty();
  }

  private List<String> toUpperCase(List<String> tags) {
    return tags.stream().map(String::toUpperCase).collect(Collectors.toList());
  }
}
