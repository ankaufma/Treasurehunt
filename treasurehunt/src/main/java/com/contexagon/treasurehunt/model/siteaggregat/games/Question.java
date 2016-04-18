package com.contexagon.treasurehunt.model.siteaggregat.games;

import java.util.List;

/**
 * Created by ankaufma on 03.02.2016.
 */
public class Question {
    private final List<Answer> answers;
    private final String id;
    private final String question;

    public Question(List<Answer> answers, String id, String question) {
        this.answers = answers;
        this.id = id;
        this.question = question;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public String getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }
}
