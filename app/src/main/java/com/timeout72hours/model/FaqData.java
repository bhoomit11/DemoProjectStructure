package com.timeout72hours.model;

import java.util.ArrayList;

/**
 * Created by bhumit on 2/12/17.
 */

public class FaqData {
    private String question,answer;

    private boolean isOpen = false;

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
