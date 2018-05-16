package com.javarush.task.task28.task2810.view;

import com.javarush.task.task28.task2810.Controller;
import com.javarush.task.task28.task2810.vo.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class HtmlView implements View {
    private final String filePath="./4.JavaCollections/src/"+this.getClass().getPackage().getName().replace(".","/")+"/vacancies.html";
            //"com/javarush/task/task28/task2810/view/vacancies.html";
    private Controller controller;
    @Override
    public void update(List<Vacancy> vacancies) {
        try {
            updateFile(getUpdatedFileContent(vacancies));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void setController(Controller controller) {
        this.controller=controller;
    }

    public void userCitySelectEmulationMethod(){
        controller.onCitySelect("Odessa");
    }

    private String getUpdatedFileContent(List<Vacancy> vacancies){
        Document document=null;
        String outputString=null;
        try {
            document=getDocument();

        Element elementWithTemplate=document.getElementsByClass("template").first();
        Element copyElementWithTemplate=elementWithTemplate.clone();
        copyElementWithTemplate.removeClass("template");
        copyElementWithTemplate.removeAttr("style");
        Elements elements=document.select("tr");
        for (Element element:elements){
            if (element.hasClass("vacancy"))
                if (!element.hasClass("template"))
                    element.remove();
        }
        for (Vacancy vacancy:vacancies){
            Element vacancyTemplate=copyElementWithTemplate.clone();
            vacancyTemplate.getElementsByClass("city").first().appendText(vacancy.getCity());
            vacancyTemplate.getElementsByClass("companyName").first().appendText(vacancy.getCompanyName());
            vacancyTemplate.getElementsByClass("salary").first().appendText(vacancy.getSalary());
            Element temp=vacancyTemplate.getElementsByTag("a").first();
            temp.appendText(vacancy.getTitle());
            temp.attr("href", vacancy.getUrl());
            elementWithTemplate.before(vacancyTemplate.outerHtml());
        }
        }catch (IOException e){
            e.printStackTrace();
            return "Some exception occurred";
        }
        return document.outerHtml();
    }

    protected Document getDocument() throws IOException {
        Document document=Jsoup.parse(new File(filePath), "UTF-8");
        return document;
    }

    private void updateFile(String file){
        try (FileOutputStream fos=new FileOutputStream(new File(filePath))){
            fos.write(file.getBytes());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
