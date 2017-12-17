package com.mierro.main.global;

/**
 * Created by 黄晓滨 simba on 2017/8/18.
 * Remarks：
 */
public class UserItemCache {


    /**
     * 投入总数
     */
    private Integer number;

    /**
     * 预竞拍计数
     */
    private Integer auction;

    /**
     * 预竞拍线程管理
     */
    private Thread thread;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getAuction() {
        return auction;
    }

    public void setAuction(Integer auction) {
        this.auction = auction;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }
}
