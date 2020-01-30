package ru.codeforensics.photomark.services;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.codeforensics.photomark.model.TestBean;

public class TestSpring {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        TestBean testBean = context.getBean("testBean", TestBean.class);
        TestBean2 testBean2 = context.getBean("testBean2", TestBean2.class);

        testBean = new TestBean("testBean");
        String tp =testBean.setName(testBean2.getName() + " Excellent!!!");

        System.out.println();
        System.out.println(testBean.getName());
        System.out.println();
        System.out.println(testBean2.getName());
        System.out.println();
        System.out.println(tp);
        System.out.println();

        context.close();

    }



}
