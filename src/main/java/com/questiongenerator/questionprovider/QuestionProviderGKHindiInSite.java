package com.questiongenerator.questionprovider;

import com.google.common.collect.Streams;
import com.questiongenerator.config.Config;
import com.questiongenerator.vo.Option;
import com.questiongenerator.vo.Question;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component("QuestionProviderGKHindiInSite")
public class QuestionProviderGKHindiInSite implements QuestionProvider {

    @Override
    public ArrayList<Question> scrapeQuestions(Document document, Config.WebSiteConfig webSiteConfig) {

        Stream questionStream = document.getElementsByClass("question").stream().
                map(question->question.getElementsByTag("b").text()).collect(Collectors.toList()).stream();

        Stream optionsStream = document.getElementsByClass("question").stream().
                map(question->question.getElementsByClass("list-unstyled").text()).collect(Collectors.toList()).stream();


        Stream answersStream = document.getElementsByClass("question").stream().
                map(question->question.getElementsByClass("btn").attr("data-answer")).
                collect(Collectors.toList()).stream();


        ArrayList<String> listQuestionOptions = (ArrayList<String>)Streams.
                zip(questionStream,optionsStream,(question, option)->{ return question+"<ChangeTag>" +option ;})
                .collect(Collectors.toCollection(ArrayList::new));

        ArrayList<String> questionnaire = (ArrayList<String>)Streams.
                zip(listQuestionOptions.stream(),answersStream,(questionOption, answer)->{ return questionOption+"<ChangeTag>" + answer ;})
                .collect(Collectors.toCollection(ArrayList::new));

        ArrayList<Question> questions = getQuestions(questionnaire);


        return questions;
    }






    private ArrayList<Question> getQuestions(ArrayList<String> questionnaire) {
        ArrayList<Question> questions = new ArrayList<Question>();

        for(String questionElement : questionnaire)
        {

            String eachElement[] = questionElement.split("<ChangeTag>");
            String questionPart = eachElement[0];
            String optionsPart  = eachElement[1];
            String answerPart  = eachElement[2];

            String opt1 = optionsPart.substring(optionsPart.indexOf("(A)" ) + 3,optionsPart.indexOf("(B)"));
            String opt2 = optionsPart.substring(optionsPart.indexOf("(B)" ) + 3,optionsPart.indexOf("(C)"));
            String opt3 = optionsPart.substring(optionsPart.indexOf("(C)") + 3,optionsPart.indexOf("(D)"));
            String opt4 = optionsPart.substring(optionsPart.indexOf("(D)") + 3 ,optionsPart.length());

            Question question = new Question();
            String questionToFill = questionPart.substring(questionPart.indexOf(" "));
            question.setQuestion(questionToFill);

            ArrayList<Option> options = new ArrayList<Option>();
            Option option1 = new Option();
            option1.setOption(opt1);

            Option option2 = new Option();
            option2.setOption(opt2);

            Option option3 = new Option();
            option3.setOption(opt3);

            Option option4 = new Option();
            option4.setOption(opt4);

            options.add(option1);
            options.add(option2);
            options.add(option3);
            options.add(option4);

            question.setOptions(options);

            String correctAnswer = "";
            String ans = answerPart.trim();
            String rightAns="";
            if(ans.trim().equalsIgnoreCase("A")){
                rightAns="1";
            }else if (ans.trim().equalsIgnoreCase("B")){
                rightAns="2";
            }else if (ans.trim().equalsIgnoreCase("C")){
                rightAns="3";
            }else if (ans.trim().equalsIgnoreCase("D")){
                rightAns="4";
            }
            if(!StringUtil.isBlank(rightAns)){
                question.setCorrect(rightAns);
                questions.add(question);
            }else {
                System.out.println("ERROR: Question does not have correct answer");
                System.out.println("Question is :" + question);
                System.out.println("Optins are   : [ " + option1+" , " + option2 +" , " +  option3+" , " + option4 +" ] ");
                System.out.println("Answer is   : [  " +rightAns + " ]");


            }



        }
        return questions;
    }


}
