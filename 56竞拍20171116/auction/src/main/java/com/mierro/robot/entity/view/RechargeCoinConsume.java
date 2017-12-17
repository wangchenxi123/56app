package com.mierro.robot.entity.view;

/**
 * 用户拍币消耗信息
 * Created by tlseek on 2017/10/8.
 */
public class RechargeCoinConsume {

    /**用户所有充值的拍币*/
    public final int rechargeSum;
    /**用户现在拥有拍币*/
    public final int current;
    /**排队竞拍预扣费*/
    public final int withholding;
    /**用户目前中奖记录的总成本*/
    public final float sealedCostSum;

    public RechargeCoinConsume(int rechargeSum, int current,int withholding, float sealedCostSum) {
        this.rechargeSum = rechargeSum;
        this.current = current;
        this.withholding = withholding;
        this.sealedCostSum = sealedCostSum;
    }

    @Override
    public String toString() {
        return "RechargeCoinConsume{" +
                "rechargeSum=" + rechargeSum +
                ", current=" + current +
                ", withholding=" + withholding +
                ", sealedCostSum=" + sealedCostSum +
                '}';
    }
}
