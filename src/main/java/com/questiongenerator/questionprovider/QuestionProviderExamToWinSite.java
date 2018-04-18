package com.questiongenerator.questionprovider;

import com.google.common.collect.Lists;
import com.questiongenerator.config.Config;
import com.questiongenerator.vo.Option;
import com.questiongenerator.vo.Question;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component("QuestionProviderExamToWinSite")
public class QuestionProviderExamToWinSite implements QuestionProvider {

    @Override
    public List<Question> scrapeQuestions(Document document, Config.WebSiteConfig webSiteConfig) {

        List<String> listCollect = document.getElementsByTag("table").
                stream().
                filter(table -> table.getElementsByTag("tr").get(0).getElementsByTag("td")
                        .get(0).getElementsByTag("p").size() > 0).
                flatMap(tableRow -> {
                    return tableRow.getElementsByTag("tr").stream();
                }).
                map(text -> text.text()).collect(Collectors.toList());


        List<List<String>> listList = Lists.partition(listCollect, 6);
        Question question = null;
        List<Option> options = null;
        List<Question> questionList = new ArrayList<Question>();

        for (List<String> list : listList) {
            question = new Question();
            String questionString = list.get(0);
            int indexOfStartQuestion = questionString.indexOf(".");

            questionString = indexOfStartQuestion > -1 ? questionString.substring(indexOfStartQuestion + 1) : questionString;
            question.setQuestion(questionString);
            options = IntStream.range(1, list.size() - 1).mapToObj(index -> {
                Option option = new Option();
                option.setOption(list.get(index).trim());
                return option;
            }).collect(Collectors.toList());
            question.setOptions(options);

            String answer = list.get(list.size() - 1).split(":")[1].trim();
            int indexCorrect = 0;
            for (Option option : options) {
                indexCorrect = indexCorrect + 1;
                if (answer.equalsIgnoreCase(option.getOption())) {

                    break;
                }
            }
            if(indexCorrect!=0){
                question.setCorrect(String.valueOf(indexCorrect));
                questionList.add(question);
            }else {
                System.out.println("ERROR: Question does not have correct answer");
                System.out.println("Question is :" + question);
                System.out.println("Optins are   : [ " + options.get(0)+" , " +  options.get(1) +" , " +   options.get(3)+" , " +  options.get(4) +" ] ");
                System.out.println("Answer is   : [  " +indexCorrect + " ]");


            }


        }

        return questionList;
    }


}
