package com.questiongenerator.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties("appconfig")
public class Config {


    public List<WebSiteConfig> getWebSiteConfigs() {
        return webSiteConfigs;
    }

    public void setWebSiteConfigs(List<WebSiteConfig> webSiteConfigs) {
        this.webSiteConfigs = webSiteConfigs;
    }

    private List<WebSiteConfig> webSiteConfigs = new ArrayList<>();

    public static class WebSiteConfig {

        @Override
        public String toString() {
            return "WebSiteConfig{" +
                    "websiteId='" + websiteId + '\'' +
                    ", provider='" + provider + '\'' +
                    ", url='" + url + '\'' +
                    ", noofpages='" + noofpages + '\'' +
                    ", generateFlag='" + generateFlag + '\'' +
                    '}';
        }

        public String getWebsiteId() {
            return websiteId;
        }

        public void setWebsiteId(String websiteId) {
            this.websiteId = websiteId;
        }

        public String getProvider() {
            return provider;
        }

        public void setProvider(String provider) {
            this.provider = provider;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getNoofpages() {
            return noofpages;
        }

        public void setNoofpages(String noofpages) {
            this.noofpages = noofpages;
        }

        public boolean isGenerateFlag() {
            return generateFlag;
        }

        public void setGenerateFlag(boolean generateFlag) {
            this.generateFlag = generateFlag;
        }

        String websiteId;
        String provider;
        String url;
        String noofpages;
        boolean generateFlag;
    }

}
