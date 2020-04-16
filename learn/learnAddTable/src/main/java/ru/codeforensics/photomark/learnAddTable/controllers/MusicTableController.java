package ru.codeforensics.photomark.learnAddTable.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.codeforensics.photomark.learnAddTable.entities.MusicTable;
import ru.codeforensics.photomark.learnAddTable.repositories.MusicTableRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@RestController
public class MusicTableController {

    private final static Logger logger = LoggerFactory.getLogger(MusicTableController.class);

    private MusicTableRepository musicTableRepository;

    @Autowired
    public MusicTableController(MusicTableRepository musicTableRepository) {
        this.musicTableRepository = musicTableRepository;
    }

    @RequestMapping("json")
    public void json() {

        File jsonFile = null;
        try {
            jsonFile = ResourceUtils.getFile("classpath:employees.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            List<MusicTable> employees = objectMapper.readValue(jsonFile, new TypeReference<List<MusicTable>>() {
            });
            musicTableRepository.saveAll(employees);

            logger.info("All records saved.");



        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
