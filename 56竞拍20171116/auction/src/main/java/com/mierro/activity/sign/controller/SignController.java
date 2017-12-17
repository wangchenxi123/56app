package com.mierro.activity.sign.controller;

import com.alibaba.fastjson.JSON;
import com.mierro.activity.sign.entity.ExchangeKey;
import com.mierro.activity.sign.service.SignService;
import com.mierro.authority.controller.SystemModelHandler;
import com.mierro.authority.entity.User;
import com.mierro.common.common.ResponseCode;
import com.mierro.common.common.ResultMessage;
import com.mierro.main.common.CoinType;
import com.mierro.activity.sign.entity.Prize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 黄晓滨 simba on 2017/8/9.
 * Remarks：
 */
@RestController
@RequestMapping("/activity")
public class SignController {

    @Resource
    private SignService signService;

    /**
     * 每日签到
     * @return 操作码
     */
    @GetMapping("/sign")
    public ResultMessage reported(@ApiIgnore @ModelAttribute(SystemModelHandler.CURRENT_USER)User user){
        Prize prize = signService.reported(user.getId());
        return new ResultMessage(ResponseCode.OK,"操作成功").putParam("resource",prize);
    }

    /**
     * 获取用户签到信息
     * @return 操作码
     */
    @GetMapping("/sign/message")
    public ResultMessage reportMessage(@ApiIgnore @ModelAttribute(SystemModelHandler.CURRENT_USER)User user){
        Map map =signService.reportMessage(user.getId());
        return new ResultMessage(ResponseCode.OK,"操作成功").putParam("resource",map);
    }

    /**
     * 领取固定奖品
     * @return 奖品码
     */
    @GetMapping("/sign/prize")
    public ResultMessage receiveTreasure(@ApiIgnore @ModelAttribute(SystemModelHandler.CURRENT_USER)User user){
        Prize prize = signService.receiveTreasure(user.getId());
        Map<String,Object> map = new HashMap<>();
        map.put("message","恭喜领取到奖品：");
        if (prize.getIntegral() != null){
            map.put("message","恭喜领取到奖品：积分"+prize.getIntegral());
            map.put("integral",prize.getIntegral());
        }
        if (prize.getCoin() != null){
            if (prize.getCoinType().equals(CoinType.GIFT)){
                map.put("message","恭喜领取到奖品：赠币"+prize.getCoin());
                map.put("integral",prize.getCoin());
            }else{
                map.put("message","恭喜领取到奖品：拍币"+prize.getCoin());
                map.put("integral",prize.getCoin());
            }
        }
        return new ResultMessage(ResponseCode.OK,"操作成功").putParam("resource",map);
    }

    /**
     * 兑换奖励
     * @param exchangeKey 兑换枚举
     * @return 操作码
     */
    @GetMapping("/sign/exchange")
    public ResultMessage exchange(@ApiIgnore @ModelAttribute(SystemModelHandler.CURRENT_USER)User user,
                                  @ModelAttribute("exchangeKey") ExchangeKey exchangeKey){
        signService.exchange(user.getId(),exchangeKey);
        return new ResultMessage(ResponseCode.OK,"操作成功");
    }

    /**
     * 设置奖励
     * @param prizesJson 奖励json集合
     * @return 操作码
     */
    @PostMapping("/sign/fixed_eward")
    public ResultMessage settingFixedReward(String prizesJson){
        List<Prize> prizes;
        try {
            prizes = JSON.parseArray(prizesJson,Prize.class);
        }catch (Exception e){
            return new ResultMessage(ResponseCode.BAD_REQUEST,"传入的数据格式有误");
        }
        signService.settingFixedReward(prizes);
        return new ResultMessage(ResponseCode.OK,"操作成功");
    }
}
