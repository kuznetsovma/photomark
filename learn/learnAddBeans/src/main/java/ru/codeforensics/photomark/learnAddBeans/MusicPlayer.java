package ru.codeforensics.photomark.learnAddBeans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.codeforensics.photomark.learnAddBeans.music.Music;


@Component
public class MusicPlayer {

    @Autowired
    @Qualifier("rockMusic")
    private Music music;

    @Value("${app.xyz.prefix}")
    private String prefix;

    public void playMusic() {
        System.out.println(prefix + " Playing: " + music.getSong());
    }
}