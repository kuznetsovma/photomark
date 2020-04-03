package ru.codeforensics.photomark.services;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.codeforensics.photomark.model.TestBean2;

public class TestSpring {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        ru.codeforensics.photomark.services.TestBean testBean = context.getBean("testBean", ru.codeforensics.photomark.services.TestBean.class);
        ru.codeforensics.photomark.model.TestBean2 testBean2 = context.getBean("testBean2", ru.codeforensics.photomark.model.TestBean2.class);

        System.out.println();
        System.out.println(testBean.getName());
        System.out.println();
        System.out.println(testBean2.getName());
        System.out.println();

//        Cat cat = new Cat();
//        cat.setName(parrot.getName() + "-killer");

        testBean = new TestBean("testBean");
        String tp =testBean.setName(testBean2.getName() + " Excellent!!!");


        System.out.println(tp);
        System.out.println();

        context.close();

    }



}
