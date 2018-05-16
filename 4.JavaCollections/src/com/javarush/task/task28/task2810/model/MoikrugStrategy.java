package com.javarush.task.task28.task2810.model;

import com.javarush.task.task28.task2810.vo.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MoikrugStrategy implements Strategy {
    private static final String URL_FORMAT = "https://moikrug.ru/vacancies?q=java+%s&page=%d";
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
                Elements elements=document.getElementsByClass("job");
                if (elements.isEmpty())
                    break;
                for (Element element : elements) {
                    if (element!=null){
                        Vacancy vacancy=new Vacancy();
                        vacancy.setTitle(element.getElementsByClass("title").text());
                        vacancy.setCity(element.getElementsByClass("location").text());
                        vacancy.setCompanyName(element.getElementsByClass("company_name").text());
                        vacancy.setUrl("https://moikrug.ru"+element.getElementsByClass("job_icon").attr("href"));
                        String salary=element.getElementsByClass("salary").text();
                        vacancy.setSalary(salary!=null?salary:"");
                        vacancy.setSiteName("https://moikrug.ru");
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

    protected Document getDocument(String searchString, int page) throws IOException {
        //String url="http://javarush.ru/testdata/big28data.html";
        String url=String.format(URL_FORMAT, searchString, page);
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
