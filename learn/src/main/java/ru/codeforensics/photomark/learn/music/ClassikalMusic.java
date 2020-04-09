package ru.codeforensics.photomark.learn.music;

import org.springframework.stereotype.Component;

@Component
public class ClassikalMusic implements Music {
    @Override
    public String getSong(){
        return "Shostakovich - Symphony N7";
    }
}
