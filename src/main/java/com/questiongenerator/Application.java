package com.questiongenerator;

import com.questiongenerator.pdfquestionreader.PDFReader;
import com.questiongenerator.questionprovider.QuestionProviderGKHindiInSite;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;

@SpringBootApplication
public class Application {

    @Resource
    QuestionProviderGKHindiInSite questionProviderGKHindiInSite;
    @Resource
    QuestionAgregator questionAgregator;

    public static void main(String[] args){


        SpringApplication.run(Application.class,args);
    }
    @Bean
    CommandLineRunner runner(){
        return args->{
            questionAgregator.generate();
            //PDFReader reader = new PDFReader();
            //reader.read();
        };
    }

}
