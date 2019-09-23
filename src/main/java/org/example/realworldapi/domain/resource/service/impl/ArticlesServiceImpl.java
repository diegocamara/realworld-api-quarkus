package org.example.realworldapi.domain.resource.service.impl;

import org.example.realworldapi.domain.entity.persistent.Article;
import org.example.realworldapi.domain.repository.UsersFollowersRepository;
import org.example.realworldapi.domain.resource.service.ArticlesService;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class ArticlesServiceImpl implements ArticlesService {

  private UsersFollowersRepository usersFollowersRepository;

  public ArticlesServiceImpl(UsersFollowersRepository usersFollowersRepository) {
    this.usersFollowersRepository = usersFollowersRepository;
  }

  @Override
  public List<Article> findRecentArticles(Long loggedUserId, int offset, int limit) {
    return usersFollowersRepository.findMostRecentArticles(loggedUserId, offset, limit);
  }
}
