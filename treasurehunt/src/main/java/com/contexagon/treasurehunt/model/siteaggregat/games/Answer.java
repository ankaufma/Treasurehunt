package com.contexagon.treasurehunt.model.siteaggregat.games;

/**
 * Created by ankaufma on 03.02.2016.
 */
public class Answer {
    private final String answer;
    private final String id;
    private final String isTrue;

    public Answer(String answer, String id, String isTrue) {
        this.answer = answer;
        this.id = id;
        this.isTrue = isTrue;
    }

    public String getAnswer() {
        return answer;
    }

    public String getId() {
        return id;
    }

    public String getIsTrue() {
        return isTrue;
    }
}
