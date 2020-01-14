package org.example.realworldapi.infrastructure.repository.panache;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Parameters;
import org.example.realworldapi.domain.model.entity.Article;
import org.example.realworldapi.domain.model.entity.Comment;
import org.example.realworldapi.domain.model.repository.ArticleRepository;

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
    return find(findArticlesQueryBuilder.toQueryString(), params)
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
  @SuppressWarnings({"rawtypes", "unchecked"})
  public List<Comment> findComments(Long articleId) {
    PanacheQuery panacheQuery =
        find(
            "select articles.comments from Article as articles where articles.id = :articleId",
            Parameters.with("articleId", articleId));
    return (List<Comment>) panacheQuery.list();
  }

  @Override
  @SuppressWarnings("rawtypes")
  public long count(List<String> tags, List<String> authors, List<String> favorited) {
    Map<String, Object> params = new LinkedHashMap<>();
    SimpleQueryBuilder countArticlesQueryBuilder = new SimpleQueryBuilder();
    countArticlesQueryBuilder.addQueryStatement("select count(*) from Article as articles");
    configFilterFindArticlesQueryBuilder(
        countArticlesQueryBuilder, tags, authors, favorited, params);
    PanacheQuery panacheQuery = find(countArticlesQueryBuilder.toQueryString(), params);
    return (long) panacheQuery.firstResult();
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
