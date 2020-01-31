package org.example.realworldapi.application;

import org.example.realworldapi.application.data.ArticleData;
import org.example.realworldapi.application.data.ArticlesData;
import org.example.realworldapi.application.data.CommentData;
import org.example.realworldapi.application.data.ProfileData;
import org.example.realworldapi.domain.model.entity.*;
import org.example.realworldapi.domain.model.exception.ArticleNotFoundException;
import org.example.realworldapi.domain.model.exception.CommentNotFoundException;
import org.example.realworldapi.domain.model.exception.FavoriteEntryNotFoundException;
import org.example.realworldapi.domain.model.exception.UserNotFoundException;
import org.example.realworldapi.domain.model.provider.SlugProvider;
import org.example.realworldapi.domain.model.repository.*;
import org.example.realworldapi.domain.service.ArticlesService;
import org.example.realworldapi.domain.service.ProfilesService;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class ArticlesServiceImpl implements ArticlesService {

  private static final int DEFAULT_LIMIT = 20;

  private ArticlesUsersRepository articlesUsersRepository;
  private ArticlesTagsRepository articlesTagsRepository;
  private UserRepository userRepository;
  private TagRepository tagRepository;
  private CommentRepository commentRepository;
  private ArticleRepository articleRepository;
  private ProfilesService profilesService;
  private SlugProvider slugProvider;

  public ArticlesServiceImpl(
      ArticlesUsersRepository articlesUsersRepository,
      ArticlesTagsRepository articlesTagsRepository,
      UserRepository userRepository,
      TagRepository tagRepository,
      ArticleRepository articleRepository,
      CommentRepository commentRepository,
      ProfilesService profilesService,
      SlugProvider slugProvider) {
    this.articlesUsersRepository = articlesUsersRepository;
    this.articlesTagsRepository = articlesTagsRepository;
    this.userRepository = userRepository;
    this.tagRepository = tagRepository;
    this.articleRepository = articleRepository;
    this.commentRepository = commentRepository;
    this.profilesService = profilesService;
    this.slugProvider = slugProvider;
  }

  @Override
  @Transactional
  public ArticlesData findRecentArticles(Long loggedUserId, int offset, int limit) {

    List<Article> articles =
        articleRepository.findMostRecentArticles(loggedUserId, offset, getLimit(limit));

    long articlesCount = articleRepository.count(loggedUserId);

    return new ArticlesData(toResultList(articles, loggedUserId), articlesCount);
  }

  @Override
  @Transactional
  public ArticlesData findArticles(
      int offset,
      int limit,
      Long loggedUserId,
      List<String> tags,
      List<String> authors,
      List<String> favorited) {

    List<Article> articles =
        articleRepository.findArticles(offset, getLimit(limit), tags, authors, favorited);

    long articlesCount = articleRepository.count(tags, authors, favorited);

    return new ArticlesData(toResultList(articles, loggedUserId), articlesCount);
  }

  @Override
  @Transactional
  public ArticleData create(
      String title, String description, String body, List<String> tagList, Long authorId) {
    Article article = createArticle(title, description, body, authorId);
    createArticlesTags(article, tagList);
    return getArticle(article, authorId);
  }

  @Override
  @Transactional
  public ArticleData findBySlug(String slug) {
    Article article = articleRepository.findBySlug(slug).orElseThrow(ArticleNotFoundException::new);
    return getArticle(article, null);
  }

  @Override
  @Transactional
  public ArticleData update(
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

    return getArticle(article, authorId);
  }

  @Override
  @Transactional
  public void delete(String slug, Long authorId) {
    Article article =
        articleRepository
            .findByIdAndSlug(authorId, slug)
            .orElseThrow(ArticleNotFoundException::new);
    articleRepository.remove(article);
  }

  @Override
  @Transactional
  public List<CommentData> findCommentsBySlug(String slug, Long loggedUserId) {
    Article article = articleRepository.findBySlug(slug).orElseThrow(ArticleNotFoundException::new);
    List<Comment> comments = commentRepository.findArticleComments(article.getId());
    return comments.stream()
        .map(
            comment -> {
              ProfileData author =
                  profilesService.getProfile(comment.getAuthor().getUsername(), loggedUserId);
              return getComment(comment, author);
            })
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public CommentData createComment(String slug, String body, Long commentAuthorId) {
    Article article = articleRepository.findBySlug(slug).orElseThrow(ArticleNotFoundException::new);
    User author =
        userRepository.findUserById(commentAuthorId).orElseThrow(UserNotFoundException::new);
    Comment comment = createComment(body, article, author);
    ProfileData authorProfile = profilesService.getProfile(author.getUsername(), author.getId());
    return getComment(comment, authorProfile);
  }

  @Override
  @Transactional
  public void deleteComment(String slug, Long commentId, Long loggedUserId) {
    Comment comment =
        commentRepository
            .findComment(slug, commentId, loggedUserId)
            .orElseThrow(CommentNotFoundException::new);
    commentRepository.remove(comment);
  }

  @Override
  @Transactional
  public ArticleData favoriteArticle(String slug, Long loggedUserId) {

    Article article = articleRepository.findBySlug(slug).orElseThrow(ArticleNotFoundException::new);

    if (!articlesUsersRepository.isFavorited(article.getId(), loggedUserId)) {

      User loggedUser =
          userRepository.findUserById(loggedUserId).orElseThrow(UserNotFoundException::new);

      ArticlesUsers articlesUsers = getArticlesUsers(article, loggedUser);

      articlesUsersRepository.create(articlesUsers);
    }

    return getArticle(article, loggedUserId);
  }

  @Override
  @Transactional
  public ArticleData unfavoriteArticle(String slug, Long loggedUserId) {

    Article article = articleRepository.findBySlug(slug).orElseThrow(ArticleNotFoundException::new);

    if (articlesUsersRepository.isFavorited(article.getId(), loggedUserId)) {

      User loggedUser =
          userRepository.findUserById(loggedUserId).orElseThrow(UserNotFoundException::new);

      ArticlesUsers articlesUsers =
          articlesUsersRepository
              .findById(getArticlesUsersKey(article, loggedUser))
              .orElseThrow(FavoriteEntryNotFoundException::new);

      articlesUsersRepository.remove(articlesUsers);
    }

    return getArticle(article, loggedUserId);
  }

  private ArticlesUsers getArticlesUsers(Article article, User loggedUser) {
    ArticlesUsersKey articlesUsersKey = getArticlesUsersKey(article, loggedUser);
    ArticlesUsers articlesUsers = new ArticlesUsers();
    articlesUsers.setPrimaryKey(articlesUsersKey);
    return articlesUsers;
  }

  private ArticlesUsersKey getArticlesUsersKey(Article article, User loggedUser) {
    ArticlesUsersKey articlesUsersKey = new ArticlesUsersKey();
    articlesUsersKey.setArticle(article);
    articlesUsersKey.setUser(loggedUser);
    return articlesUsersKey;
  }

  private Comment createComment(String body, Article article, User author) {
    Comment comment = new Comment();
    comment.setArticle(article);
    comment.setAuthor(author);
    comment.setBody(body);
    return commentRepository.create(comment);
  }

  private CommentData getComment(Comment comment, ProfileData authorProfile) {
    return new CommentData(
        comment.getId(),
        comment.getCreatedAt(),
        comment.getUpdatedAt(),
        comment.getBody(),
        authorProfile);
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
    article.setAuthor(userRepository.findUserById(userId).orElseThrow(UserNotFoundException::new));
    articleRepository.create(article);
    return article;
  }

  private void configSlug(String title, Article article) {
    String slug = slugProvider.slugify(title);
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

  private List<ArticleData> toResultList(List<Article> articles, Long loggedUserId) {
    return articles.stream()
        .map(article -> getArticle(article, loggedUserId))
        .collect(Collectors.toList());
  }

  private ArticleData getArticle(Article article, Long loggedUserId) {
    boolean isFavorited = false;

    if (loggedUserId != null) {
      isFavorited = articlesUsersRepository.isFavorited(article.getId(), loggedUserId);
    }

    long favoritesCount = articlesUsersRepository.favoritesCount(article.getId());

    ProfileData author =
        profilesService.getProfile(article.getAuthor().getUsername(), loggedUserId);

    List<String> tags =
        tagRepository.findArticleTags(article.getId()).stream()
            .map(Tag::getName)
            .collect(Collectors.toList());
    return new ArticleData(
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
