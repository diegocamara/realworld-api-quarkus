package org.example.realworldapi.application.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArticlesData {
  private List<ArticleData> articles;
  private long articlesCount;
}
