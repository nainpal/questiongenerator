package com.questiongenerator.questionprovider;

import com.questiongenerator.config.Config;
import com.questiongenerator.vo.Question;
import org.jsoup.nodes.Document;

import java.util.List;

public interface QuestionProvider {

    public List<Question> scrapeQuestions(Document document, Config.WebSiteConfig webSiteConfig);

}
