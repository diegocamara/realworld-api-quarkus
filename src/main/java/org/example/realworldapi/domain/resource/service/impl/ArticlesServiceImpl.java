package org.example.realworldapi.domain.resource.service.impl;

import com.github.slugify.Slugify;
import org.example.realworldapi.domain.entity.Profile;
import org.example.realworldapi.domain.entity.persistent.*;
import org.example.realworldapi.domain.exception.ArticleNotFoundException;
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
  private CommentRepository commentRepository;
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
      CommentRepository commentRepository,
      ProfilesService profilesService,
      Slugify slugify) {
    this.usersFollowersRepository = usersFollowersRepository;
    this.articlesUsersRepository = articlesUsersRepository;
    this.articlesTagsRepository = articlesTagsRepository;
    this.userRepository = userRepository;
    this.tagRepository = tagRepository;
    this.articleRepository = articleRepository;
    this.commentRepository = commentRepository;
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
      String title, String description, String body, List<String> tagList, Long authorId) {
    Article article = createArticle(title, description, body, authorId);
    createArticlesTags(article, tagList);
    return getArticle(article, authorId);
  }

  @Override
  @Transactional
  public org.example.realworldapi.domain.entity.Article findBySlug(String slug) {
    Article article = articleRepository.findBySlug(slug).orElseThrow(ArticleNotFoundException::new);
    return getArticle(article, null);
  }

  @Override
  @Transactional
  public org.example.realworldapi.domain.entity.Article update(
      String slug, String title, String description, String body, Long authorId) {

    Article article = articleRepository.findBySlug(slug).orElseThrow(ArticleNotFoundException::new);

    if (isPresent(title)) {
      configTitle(title, article);
    }

    if (isPresent(description)) {
      article.setDescription(description);
    }

    if (isPresent(body)) {
      article.setBody(body);
    }

    return getArticle(articleRepository.update(article), authorId);
  }

  @Override
  @Transactional
  public void delete(String slug, Long authorId) {
    Article article =
        articleRepository
            .findByIdAndSlug(authorId, slug)
            .orElseThrow(ArticleNotFoundException::new);
    articleRepository.delete(article);
  }

  @Override
  @Transactional
  public List<org.example.realworldapi.domain.entity.Comment> findCommentsBySlug(
      String slug, Long loggedUserId) {
    Article article = articleRepository.findBySlug(slug).orElseThrow(ArticleNotFoundException::new);
    List<Comment> comments = articleRepository.findComments(article.getId());
    Profile author = profilesService.getProfile(article.getAuthor().getUsername(), loggedUserId);
    return getComments(comments, author);
  }

  @Override
  @Transactional
  public org.example.realworldapi.domain.entity.Comment createComment(
      String slug, String body, Long commentAuthorId) {
    Article article = articleRepository.findBySlug(slug).orElseThrow(ArticleNotFoundException::new);
    User author = userRepository.findById(commentAuthorId).orElseThrow(UserNotFoundException::new);
    Comment comment = createComment(body, article, author);
    Profile authorProfile = profilesService.getProfile(author.getUsername(), author.getId());
    return getComment(comment, authorProfile);
  }

  @Override
  @Transactional
  public void deleteComment(String slug, Long commentId, Long loggedUserId) {
    Comment comment = commentRepository.findComment(slug, commentId, loggedUserId);
    commentRepository.delete(comment);
  }

  private Comment createComment(String body, Article article, User author) {
    Comment comment = new Comment();
    comment.setArticle(article);
    comment.setAuthor(author);
    comment.setBody(body);
    return commentRepository.create(comment);
  }

  private List<org.example.realworldapi.domain.entity.Comment> getComments(
      List<Comment> comments, Profile profile) {
    return comments.stream()
        .map(comment -> getComment(comment, profile))
        .collect(Collectors.toList());
  }

  private org.example.realworldapi.domain.entity.Comment getComment(
      Comment comment, Profile profile) {
    return new org.example.realworldapi.domain.entity.Comment(
        comment.getId(),
        comment.getCreatedAt(),
        comment.getUpdatedAt(),
        comment.getBody(),
        profile);
  }

  private void configTitle(String title, Article article) {
    configSlug(title, article);
    article.setTitle(title);
  }

  private Article createArticle(String title, String description, String body, Long userId) {
    Article article = new Article();
    configTitle(title, article);
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

  private boolean isPresent(String value) {
    return value != null && !value.isEmpty();
  }
}
