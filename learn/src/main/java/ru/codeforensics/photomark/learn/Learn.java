package ru.codeforensics.photomark.learn;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@SpringBootApplication
    public class Learn {

        public static void main(String[] args) {
            SpringApplication.run(Learn.class, args);

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        Music music = context.getBean("rockMusic", Music.class);
        Music music2 = context.getBean("classikalMusic", Music.class);

        MusicPlayer musicPlayer = new MusicPlayer(music);
        MusicPlayer musicPlayer2 = new MusicPlayer(music2);

        System.out.println();

        musicPlayer.playMusic();
        musicPlayer2.playMusic();

        System.out.println();

        context.close();

        }

}
