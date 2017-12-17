package com.mierro.main.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 黄晓滨 simba on 2017/8/8.
 * Remarks：
 */
public enum  RunningProgram {

    NOTROBOT("不使用机器人"),
    CONTROL_LINE_FIX("设死机器人控线"),
    ROBOT_SUCCESS("机器人必中"),
    CONTROL_LINE_RANGE("设置控线区间1")
    ;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    RunningProgram(String name) {
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

    public static List<ReturnName> getRunningProgramName(){
        List<ReturnName> list = new ArrayList<>();
        for (RunningProgram type: values()) {
            ReturnName returnName = new ReturnName();
            returnName.chinese = type.getName();
            returnName.english = type.toString();
            list.add(returnName);
        }
        return list;
    }
}
