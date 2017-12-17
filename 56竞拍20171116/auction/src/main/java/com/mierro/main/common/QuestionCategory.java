package com.mierro.main.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 黄晓滨 simba on 2017/8/15.
 * Remarks：
 */
public enum QuestionCategory {

    LOGIN_QUESTION("注册登录"),
    AUCTION_QUESTION("拍卖问题"),
    SUM_ALONE_REWARD("晒单奖励"),
    COMMON_QUESTION("常见问题")
    ;

    private String name;

    public static class Question{
        String chinese;

        String english;

        public String getChinese() {
            return chinese;
        }

        public void setChinese(String chinese) {
            this.chinese = chinese;
        }

        public String getEnglish() {
            return english;
        }

        public void setEnglish(String english) {
            this.english = english;
        }
    }

    public static List<Question> getQuestionType(){
        List<Question> list = new ArrayList<>();
        for (QuestionCategory type: values()) {
            Question returnName = new Question();
            returnName.chinese = type.getName();
            returnName.english = type.toString();
            list.add(returnName);
        }
        return list;
    }

    QuestionCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
