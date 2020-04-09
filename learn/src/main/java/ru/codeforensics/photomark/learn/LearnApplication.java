package ru.codeforensics.photomark.learn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class LearnApplication {

    public static void main(String[] args) {
        SpringApplication.run(LearnApplication.class, args);

        System.out.println("Hello word!");
        System.out.println("Hello word!");
        System.out.println("Hello word!");
        System.out.println("Hello word!");

//        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
//
//        Music music = context.getBean("rockMusic", Music.class);
//        Music music2 = context.getBean("classikalMusic", Music.class);
//
//        MusicPlayer musicPlayer = new MusicPlayer(music);
//        MusicPlayer musicPlayer2 = new MusicPlayer(music2);
//
//		System.out.println();
//
//        musicPlayer.playMusic();
//        musicPlayer2.playMusic();
//
//        System.out.println();
//
//        context.close();

    }

}
