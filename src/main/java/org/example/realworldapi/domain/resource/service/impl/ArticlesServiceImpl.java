package org.example.realworldapi.domain.resource.service.impl;

import com.github.slugify.Slugify;
import org.example.realworldapi.domain.entity.Profile;
import org.example.realworldapi.domain.entity.persistent.Article;
import org.example.realworldapi.domain.entity.persistent.ArticlesTags;
import org.example.realworldapi.domain.entity.persistent.ArticlesTagsKey;
import org.example.realworldapi.domain.entity.persistent.Tag;
import org.example.realworldapi.domain.exception.UserNotFoundException;
import org.example.realworldapi.domain.repository.*;
import org.example.realworldapi.domain.resource.service.ArticlesService;
import org.example.realworldapi.domain.resource.service.ProfilesService;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class ArticlesServiceImpl implements ArticlesService {

  private static final int DEFAULT_LIMIT = 20;
  private UsersFollowersRepository usersFollowersRepository;
  private ArticlesUsersRepository articlesUsersRepository;
  private ArticlesTagsRepository articlesTagsRepository;
  private UserRepository userRepository;
  private TagRepository tagRepository;
  private ArticleRepository articleRepository;
  private ProfilesService profilesService;
  private Slugify slugify;

  public ArticlesServiceImpl(
      UsersFollowersRepository usersFollowersRepository,
      ArticlesUsersRepository articlesUsersRepository,
      ArticlesTagsRepository articlesTagsRepository,
      UserRepository userRepository,
      TagRepository tagRepository,
      ArticleRepository articleRepository,
      ProfilesService profilesService,
      Slugify slugify) {
    this.usersFollowersRepository = usersFollowersRepository;
    this.articlesUsersRepository = articlesUsersRepository;
    this.articlesTagsRepository = articlesTagsRepository;
    this.userRepository = userRepository;
    this.tagRepository = tagRepository;
    this.articleRepository = articleRepository;
    this.profilesService = profilesService;
    this.slugify = slugify;
  }

  @Override
  @Transactional
  public List<org.example.realworldapi.domain.entity.Article> findRecentArticles(
      Long loggedUserId, int offset, int limit) {

    List<Article> articles =
        usersFollowersRepository.findMostRecentArticles(loggedUserId, offset, getLimit(limit));

    return toResultList(articles, loggedUserId);
  }

  @Override
  @Transactional
  public List<org.example.realworldapi.domain.entity.Article> findArticles(
      int offset,
      int limit,
      Long loggedUserId,
      List<String> tags,
      List<String> authors,
      List<String> favorited) {

    List<Article> articles =
        articleRepository.findArticles(offset, getLimit(limit), tags, authors, favorited);

    return toResultList(articles, loggedUserId);
  }

  @Override
  @Transactional
  public org.example.realworldapi.domain.entity.Article create(
      String title, String description, String body, List<String> tagList, Long userId) {
    Article article = createArticle(title, description, body, userId);
    createArticlesTags(article, tagList);
    return getArticle(article, userId);
  }

  @Override
  @Transactional
  public org.example.realworldapi.domain.entity.Article findBySlug(String slug) {
    Article article = articleRepository.findBySlug(slug);
    return getArticle(article, null);
  }

  private Article createArticle(String title, String description, String body, Long userId) {
    Article article = new Article();
    configSlug(title, article);
    article.setTitle(title);
    article.setDescription(description);
    article.setBody(body);
    article.setAuthor(userRepository.findById(userId).orElseThrow(UserNotFoundException::new));
    articleRepository.create(article);
    return article;
  }

  private void configSlug(String title, Article article) {
    String slug = slugify.slugify(title);
    if (articleRepository.existsBySlug(slug)) {
      slug += UUID.randomUUID().toString();
    }
    article.setSlug(slug);
  }

  private void createArticlesTags(Article article, List<String> tagList) {
    tagList.forEach(
        tagName -> {
          Optional<Tag> tagOptional = tagRepository.findByName(tagName);

          Tag tag = tagOptional.orElseGet(() -> createTag(tagName));

          ArticlesTags articlesTags = createArticlesTags(article, tag);
          articlesTagsRepository.create(articlesTags);
        });
  }

  private Tag createTag(String tagName) {
    return tagRepository.create(new Tag(tagName));
  }

  private ArticlesTags createArticlesTags(Article article, Tag tag) {
    ArticlesTagsKey articlesTagsKey = new ArticlesTagsKey(article, tag);
    return new ArticlesTags(articlesTagsKey);
  }

  private List<org.example.realworldapi.domain.entity.Article> toResultList(
      List<Article> articles, Long loggedUserId) {
    return articles.stream()
        .map(article -> getArticle(article, loggedUserId))
        .collect(Collectors.toList());
  }

  private org.example.realworldapi.domain.entity.Article getArticle(
      Article article, Long loggedUserId) {
    boolean isFavorited = false;

    if (loggedUserId != null) {
      isFavorited = articlesUsersRepository.isFavorited(article.getId(), loggedUserId);
    }

    int favoritesCount = articlesUsersRepository.favoritesCount(article.getId());

    Profile author = profilesService.getProfile(article.getAuthor().getUsername(), loggedUserId);

    List<String> tags =
        articlesTagsRepository.findTags(article.getId()).stream()
            .map(Tag::getName)
            .collect(Collectors.toList());
    return new org.example.realworldapi.domain.entity.Article(
        article.getSlug(),
        article.getTitle(),
        article.getDescription(),
        article.getBody(),
        tags,
        isFavorited,
        favoritesCount,
        article.getCreatedAt(),
        article.getUpdatedAt(),
        author);
  }

  private int getLimit(int limit) {
    return limit > 0 ? limit : DEFAULT_LIMIT;
  }
}
