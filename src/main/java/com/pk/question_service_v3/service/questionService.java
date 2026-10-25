package com.pk.question_service_v3.service;



import com.pk.question_service_v3.Repository.questionRepo;
import com.pk.question_service_v3.model.question;
import com.pk.question_service_v3.model.questionWrapper;
import com.pk.question_service_v3.model.userResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class questionService {

    @Autowired
    private questionRepo qr;
    public List<question> getQuestions() {
        return qr.findAll();
    }

    public question add(question q) {
        return qr.save(q);
    }

    public question update(question q) {
        return qr.save(q);
    }

    public void delete(int id)
    {
        qr.deleteById(id);
    }

    public List<Integer> generateQuestions(String category, Integer numq) {

        return qr.findRandomQuestionsByCategory(category,numq);
    }

    public List<questionWrapper> getQuestionsByIds(List<Integer> qids) {

        List<question> questions = qr.findAllById(qids);
        List<questionWrapper> questionWrappers = new ArrayList<>();
        for(question q: questions)
        {
            questionWrapper qw = new questionWrapper(q.getId(),q.getQuestion_title(),q.getOption1(),q.getOption2(),q.getOption3(),q.getOption4());
            questionWrappers.add(qw);
        }

        return questionWrappers;
    }

    public Integer getScore(List<userResponse> ur) {
        int result =0,i=0;
        List<Optional<question>> questions = new ArrayList<>();
        for(userResponse u: ur) {
            Optional<question> ques = qr.findById(u.getId());
            questions.add(ques);
        }
        for(Optional<question> question : questions)
        {
            if(question.get().getRight_answer().equalsIgnoreCase(ur.get(i).getResponse()))
                result++;
            i++;
        }
        return result;
    }
    }

