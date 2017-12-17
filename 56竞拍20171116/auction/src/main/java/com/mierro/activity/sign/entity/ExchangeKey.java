package com.mierro.activity.sign.entity;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 黄晓滨 simba on 2017/8/9.
 * Remarks：
 */
public enum ExchangeKey {

    ONE_GIFT("兑换一个赠币",1,100),
    TWO_GIFT("兑换两个赠币",2,200),
    THREE_GIFT("兑换三个赠币",3,300),
    FIVE_GIFT("兑换五个赠币",5,500);

    private String name;

    private Integer integral;

    private Integer coin;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    public Integer getCoin() {
        return coin;
    }

    public void setCoin(Integer coin) {
        this.coin = coin;
    }

    ExchangeKey(String name, int coin, int integral) {
        this.name = name;
        this.coin = coin;
        this.integral = integral;
    }

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

    public static List<ExchangeKey.Question> getQuestionType(){
        List<ExchangeKey.Question> list = new ArrayList<>();
        for (ExchangeKey type: values()) {
            ExchangeKey.Question returnName = new ExchangeKey.Question();
            returnName.chinese = type.getName();
            returnName.english = type.toString();
            list.add(returnName);
        }
        return list;
    }
}
