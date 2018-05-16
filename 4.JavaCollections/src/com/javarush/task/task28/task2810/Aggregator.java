package com.javarush.task.task28.task2810;

import com.javarush.task.task28.task2810.model.*;
import com.javarush.task.task28.task2810.view.HtmlView;
import com.javarush.task.task28.task2810.view.View;
import com.javarush.task.task28.task2810.vo.Vacancy;

import java.util.List;

public class Aggregator {
    public static void main(String[] args) {
        Strategy strategy=new HHStrategy();
        Provider provider=new Provider(strategy);
        Provider provider1=new Provider(new MoikrugStrategy());
        View view=new HtmlView();
        Model model=new Model(view, provider, provider1);
        Controller controller=new Controller(model);
        view.setController(controller);
        ((HtmlView) view).userCitySelectEmulationMethod();
    }
}
