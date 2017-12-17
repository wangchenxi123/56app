package com.mierro.robot.dao;

import com.mierro.main.common.CoinSource;
import com.mierro.main.common.CoinType;
import com.mierro.main.entity.CoinBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by tlseek on 2017/10/8.
 */
public interface RobotCoinDao extends JpaRepository<CoinBean,Long> {

    /**
     * 根据用户id和钱币类型查找钱币充值总数
     * @param userId 用户id
     * @param coinType 钱币类型
     * @return 钱币总数
     */
    @Query("select sum (coin.number) from CoinBean coin where coin.userId = ?1 and coin.coinType = ?2 and coin.source = ?3")
    Integer findRechargeByUserIdAndCoinType(Long userId, CoinType coinType, CoinSource coinSource);

    /**
     * 根据用户id和钱币类型查找钱币总数
     * @param userId 用户id
     * @param coinType 钱币类型
     * @return 钱币总数
     */
    @Query("select sum (coin.number) from CoinBean coin where coin.userId = ?1 and coin.coinType = ?2")
    Integer findByUserIdAndCoinType(Long userId, CoinType coinType);
}
