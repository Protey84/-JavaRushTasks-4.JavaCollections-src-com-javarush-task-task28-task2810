package com.javarush.task.task28.task2810.model;

import com.javarush.task.task28.task2810.vo.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HHStrategy implements Strategy {
    private static final String URL_FORMAT = "http://hh.ua/search/vacancy?text=java+%s&page=%d";

    @Override
    public List<Vacancy> getVacancies(String searchString) {
        List<Vacancy> list=new ArrayList<>();
        Document document=null;
        int pageNumber=0;
        while (true) {
            try {
                document = getDocument(searchString, pageNumber);
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
            try {
                Elements elements=document.select("[data-qa=vacancy-serp__vacancy]");
                if (elements.isEmpty())
                    break;
                for (Element element : elements) {
                    if (element!=null){
                        Vacancy vacancy=new Vacancy();
                        vacancy.setTitle(element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-title").text());
                        vacancy.setCity(element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-address").text());
                        vacancy.setCompanyName(element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-employer").text());
                        vacancy.setUrl(element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-title").attr("href"));
                        String salary=element.getElementsByClass("b-vacancy-list-salary").text();
                        vacancy.setSalary(salary!=null?salary:"");
                        vacancy.setSiteName("http://hh.ua");
                        list.add(vacancy);
                    }
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                break;
            }
            pageNumber++;
        }
        return list;

    }

    protected Document getDocument(String searchString, int page) throws IOException{
        //String url="http://javarush.ru/testdata/big28data.html";
        String url=String.format(URL_FORMAT, searchString,page);
        String userAgent="Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/65.0.3325.181 Chrome/65.0.3325.181 Safari/537.36";
        String referrer="no-referrer-when-downgrade";
        Document doc=null;
        try {
            doc =Jsoup.connect(url).userAgent(userAgent).referrer(referrer).get();
        } catch (IOException e){
            System.out.println("Wrong URL");
        }
        return doc;

    }
}
