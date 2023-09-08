package ru.nsu.fit.crocodile.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
@Slf4j
public class AnswerGeneratorService {

    List<String> answersList;
    Random random = new Random();

    public AnswerGeneratorService(){
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        try(InputStream is = classloader.getResourceAsStream("class_names_ru.txt")) {
            Scanner s = new Scanner(is);
            answersList = new ArrayList<>();
            while (s.hasNext()) {
                answersList.add(s.next());
            }
            s.close();
            is.close();
        } catch (IOException e) {
            log.warn(e.toString());
        }
    }

    public Set<String> generate(){

        Set<String> answers = new HashSet<>();
        for (int i = 0; i < 3; i++) {
            answers.add(answersList.get(random.nextInt(answersList.size())));
        }
        return answers;
    }
}
