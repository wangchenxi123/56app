package com.mierro.main.common;

/**
 * Created by 黄晓滨 simba on 2017/8/9.
 * Remarks：deduction
 */
public enum CoinSource {

    RECHARGE("充值"),
    Gift("赠送"),
    ABNORMAL("异常"),
    USE("使用扣除"),
    RETURN("赠送返还"),
    SYSTEM_DEDUCTION("系统扣除"),
    WITHHOLDING_FEE("排队竞拍预扣费");

    CoinSource(String name) {
    }
}
