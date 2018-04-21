package com.questiongenerator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.questiongenerator.config.Config;
import com.questiongenerator.questionprovider.QuestionProvider;
import com.questiongenerator.questionprovider.QuestionProviderFactory;
import com.questiongenerator.vo.Question;
import com.questiongenerator.vo.QuestionsCollection;
import org.json.simple.JSONArray;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
public class QuestionAgregator {


    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    @Autowired
    Config config;


    @Autowired
    @Qualifier("questionProviderFactory")
    QuestionProviderFactory questionProviderFactory;


    @Value("${question.out.file}")
    String questionOutFile;

    public void generate() {

        System.out.println("config:" + config.getWebSiteConfigs());

        List<Question> allProviderQuestion = new ArrayList<>();


        //for each config/site present in application.properties iterate and get document.
        for (Config.WebSiteConfig webSiteConfig : config.getWebSiteConfigs()) {
            LOGGER.debug("Processing for webSiteConfig:"+webSiteConfig);
            //go for generating only if flag is set to true for this site.
            if (webSiteConfig.isGenerateFlag()) {


                Document document = null;

                try {

                    String urlOfWebsite = webSiteConfig.getUrl();
                    //config specifying all the pages for this sites for question
                    //ex we can provide digit,range of digit ex 7 or 3-7 or 1-7,9,10
                    //only one digit means ie 7 then it means question in pages from one to 7 to be fetched
                    //3-7 mean pages from question from pages 2 to 7 would be faced.
                    //1-7,9,10 means question from 1 to 7 pages plus question from 9 and 10 the page as well.

                    String noOfTimesPattern = webSiteConfig.getNoofpages();
                    //get the the number of pages to be fetched for site.
                    int [] pagesNos = getPages(noOfTimesPattern) ;
                    //default page to be fetched if anything goes from the the pattern specified.
                    int [] pagesNosDefault = new int[]{ 1 };

                    ArrayList<String> urls = new ArrayList<>();
                    //Create all the urls based on the pages to get that documents
                    IntStream.of(pagesNos.length > 0 ? pagesNos : pagesNosDefault).
                            forEach(index->urls.add(String.format(urlOfWebsite,index )));

                    //load document for each url.
                    for(String url:urls){

                        try
                        {

                            document = Jsoup.connect(url).get();

                        }catch (Exception exce){
                            exce.printStackTrace();
                            System.out.println("ERROR:Skipping question for url due to error : [ " + url + " ]");
                        }

                        if (document == null) {
                            System.out.println("Error: no document loaded for url : [ " + webSiteConfig + " ].So continue with next url.");
                            continue;
                        }

                        //get the question from the provider.

                        if (!StringUtils.isEmpty(webSiteConfig.getProvider())) {

                            QuestionProvider provider = questionProviderFactory.getQuestionProvider(webSiteConfig.getProvider());
                            List<Question> arrayQuestion = provider.scrapeQuestions(document, webSiteConfig);
                            System.out.println("Original size of array is of size : " + allProviderQuestion.size());
                            allProviderQuestion.addAll(arrayQuestion);
                            System.out.println("Question in url : [ " + url +" ] is of size : " + arrayQuestion.size());
                            System.out.println("Question are  : [ "+arrayQuestion +" ] ");

                        }
                    }



                } catch (Exception exception) {
                    exception.printStackTrace();
                    System.out.println("ERROR:Skipping questions for url due to error : " +exception.getMessage());
                }

            }

        }

        System.out.println("Final Size of question schuffle:"+allProviderQuestion.size());

        System.out.println("Before schuffle:"+allProviderQuestion);
        //Collections.shuffle(allProviderQuestion);
        System.out.println("After schuffle:"+allProviderQuestion);

        QuestionsCollection questionsCollection = new QuestionsCollection();

        JSONArray jsonArray = getJsonArrayToWrite(allProviderQuestion, questionsCollection);
        try {
            writeToFile(jsonArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Done with processing.....");
    }

    public void writeToFile(JSONArray jsonArray) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        String fileContent = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonArray);
        Files.write(Paths.get(questionOutFile), fileContent.getBytes());
    }

    public JSONArray getJsonArrayToWrite(List<Question> allProviderQuestion, QuestionsCollection questionsCollection) {
        questionsCollection.setQuestions(allProviderQuestion);

        ArrayList<QuestionsCollection> questionsCollections = new ArrayList<QuestionsCollection>();
        questionsCollections.add(questionsCollection);
        JSONArray jsonArray = new JSONArray();
        questionsCollections.stream().forEach(questionsCollection1 -> jsonArray.add(questionsCollection1));
        return jsonArray;
    }

    private int [] getPages(String noOfPagePatterns){

        ArrayList<Integer> integers = new ArrayList<Integer>();

        String [] arr = noOfPagePatterns.split(",");

        for (String intValue : arr){
            String range[] = intValue.split("-");
            if(range.length > 1){
                String startRange = range[0];
                String endRange = range[1];
                if(Integer.valueOf(endRange) > Integer.valueOf(startRange)){
                    List<Integer> integersInRange =
                            IntStream.rangeClosed(Integer.valueOf(startRange),Integer.valueOf(endRange)).
                                    mapToObj(i->i).collect(Collectors.toList());
                    integers.addAll(integersInRange);
                }else {
                    System.out.println("Rejecting this range as not correct range:" + intValue);
                }
            }else{

                integers.add(Integer.valueOf(intValue));

            }

        }

        Stream<Integer> distinctStreamOfIntegers = integers.stream().distinct();

        int[] array = distinctStreamOfIntegers.mapToInt(i->i).toArray();
        return array ;
    }


}
