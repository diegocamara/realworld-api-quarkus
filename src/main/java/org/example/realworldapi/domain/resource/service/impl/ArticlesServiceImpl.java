package org.example.realworldapi.domain.resource.service.impl;

import org.example.realworldapi.domain.entity.Profile;
import org.example.realworldapi.domain.entity.persistent.Article;
import org.example.realworldapi.domain.entity.persistent.Tag;
import org.example.realworldapi.domain.repository.ArticleRepository;
import org.example.realworldapi.domain.repository.ArticlesTagsRepository;
import org.example.realworldapi.domain.repository.ArticlesUsersRepository;
import org.example.realworldapi.domain.repository.UsersFollowersRepository;
import org.example.realworldapi.domain.resource.service.ArticlesService;
import org.example.realworldapi.domain.resource.service.ProfilesService;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ArticlesServiceImpl implements ArticlesService {

  private static final int DEFAULT_LIMIT = 20;
  private UsersFollowersRepository usersFollowersRepository;
  private ArticlesUsersRepository articlesUsersRepository;
  private ArticlesTagsRepository articlesTagsRepository;
  private ArticleRepository articleRepository;
  private ProfilesService profilesService;

  public ArticlesServiceImpl(
      UsersFollowersRepository usersFollowersRepository,
      ArticlesUsersRepository articlesUsersRepository,
      ArticlesTagsRepository articlesTagsRepository,
      ArticleRepository articleRepository,
      ProfilesService profilesService) {
    this.usersFollowersRepository = usersFollowersRepository;
    this.articlesUsersRepository = articlesUsersRepository;
    this.articlesTagsRepository = articlesTagsRepository;
    this.articleRepository = articleRepository;
    this.profilesService = profilesService;
  }

  @Override
  @Transactional
  public List<org.example.realworldapi.domain.entity.Article> findRecentArticles(
      Long loggedUserId, int offset, int limit) {

    List<Article> articles =
        usersFollowersRepository.findMostRecentArticles(loggedUserId, offset, getLimit(limit));

    return toResultList(loggedUserId, articles);
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

    return toResultList(loggedUserId, articles);
  }

  private List<org.example.realworldapi.domain.entity.Article> toResultList(
      Long loggedUserId, List<Article> articles) {
    return articles.stream()
        .map(
            article -> {
              boolean isFavorited = false;

              if (loggedUserId != null) {
                isFavorited = articlesUsersRepository.isFavorited(article.getId(), loggedUserId);
              }

              int favoritesCount = articlesUsersRepository.favoritesCount(article.getId());

              Profile author =
                  profilesService.getProfile(article.getAuthor().getUsername(), loggedUserId);

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
            })
        .collect(Collectors.toList());
  }

  private int getLimit(int limit) {
    return limit > 0 ? limit : DEFAULT_LIMIT;
  }
}
