package com.mierro.authority.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 黄晓滨 simba on 2017/5/27.
 * Remarks：
 */
public enum AythorityType {

    CLIENT("普通会员"),
    INSIDER("内部人员"),
    CHANNEL("渠道商");//渠道商权限

    private String name;

    AythorityType(String name) {
        this.name = name;
    }

    public static AythorityType parse(String aythorityType){
        for (AythorityType type: values()) {
            if (type.name().equals(aythorityType) ) return type;
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static class ReturnName{
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

    public static List<ReturnName> getAythorityName(){
        List<ReturnName> list = new ArrayList<>();
        for (AythorityType type: values()) {
            ReturnName returnName = new ReturnName();
            returnName.chinese = type.getName();
            returnName.english = type.toString();
            list.add(returnName);
        }
        return list;
    }
}
