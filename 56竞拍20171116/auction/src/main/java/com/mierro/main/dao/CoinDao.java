package com.mierro.main.dao;

import com.mierro.main.common.CoinSource;
import com.mierro.main.common.CoinType;
import com.mierro.main.entity.CoinBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * Created by 黄晓滨 simba on 2017/7/29.
 * Remarks：
 */
public interface CoinDao extends JpaRepository<CoinBean,Long>{

    /**
     * 根据用户id和钱币类型查找钱币总数
     * @param userId 用户id
     * @param place 消耗去处
     * @param coinType 钱币类型
     * @return 钱币总数
     */
    @Query("select sum (coin.number) from CoinBean coin where coin.userId = ?1 and coin.place = ?2 and coin.coinType = ?3")
    Integer findByUserIdAndPlaceAndCoinType(Long userId, Long place, CoinType coinType);

    /**
     * 根据用户id和钱币类型查找钱币总数
     * @param userId 用户id
     * @param coinType 钱币类型
     * @return 钱币总数
     */
    @Query("select sum (coin.number) from CoinBean coin where coin.userId = ?1 and coin.coinType = ?2")
    Integer findByUserIdAndCoinType(Long userId, CoinType coinType);

    /**
     * 根据用户id和钱币类型查找钱币充值总数
     * @param userId 用户id
     * @param coinType 钱币类型
     * @return 钱币总数
     */
    @Query("select sum (coin.number) from CoinBean coin where coin.userId = ?1 and coin.coinType = ?2 " +
            "and coin.source = ?3 and coin.number >0")
    Integer findRechargeByUserIdAndCoinType(Long userId, CoinType coinType, CoinSource coinSource);

    /**
     * 根据用户id和钱币类型查找钱币总数
     * @param userId 用户id
     * @param coinType 钱币类型
     * @return 钱币总数
     */
    @Query("select sum (coin.number) from CoinBean coin where coin.userId = ?1 and coin.coinType = ?2 " +
            "and coin.source = ?3 and coin.number > 0")
    Integer findConsumptionByUserIdAndCoinType(Long userId, CoinType coinType, CoinSource coinSource);

    /**
     * 根据用户id和钱币类型查找钱币消耗总数
     * @param userId 用户id
     * @param coinType 钱币类型
     * @return 钱币总数
     */
    @Query("select sum (coin.number) from CoinBean coin where coin.userId = ?1 and coin.coinType = ?2 " +
            "and coin.source = ?3 and coin.number < 0")
    Integer findConsumptionByUserIdAndCoinTypeAndUse(Long userId, CoinType coinType, CoinSource coinSource);

    /**
     * 根据用户id和钱币类型查找钱币总数
     * @param userId 用户id
     * @param coinType 钱币类型
     * @param coinType2 钱币类型
     * @return 钱币总数
     */
    @Query("select sum(coin.number) from CoinBean coin where coin.userId = ?1 and (coin.coinType = ?2 or coin.coinType = ?3)")
    Integer findByUserIdAndCoinType(Long userId, CoinType coinType, CoinType coinType2);

    @Query("select sum(coin.number) from CoinBean coin where coin.userId = ?1 and coin.place = ?2 and coin.coinType = ?3")
    Integer findByUserIdAndPlace(Long userId, Long place, CoinType CoinType);

    @Query("select coin from CoinBean coin where coin.userId = ?1 and coin.place = ?2 and coin.coinType = ?3 and coin.source = ?4 ")
    CoinBean findCoinBeanByUserIdAndPlace(Long userId, Long place, CoinType CoinType, CoinSource coinSource);

    @Query("select sum(coin.number) from CoinBean coin where coin.coinType = ?1 and coin.source = ?2 " +
            "and coin.time >= ?3 and coin.time<?4")
    Integer findByCoinTypeAndCoinSourceAndTime(CoinType CoinType, CoinSource coinSource, Date startTime, Date endTime);

    @Query("select sum(coin.number) from CoinBean coin where coin.userId in ?1 and coin.coinType = ?2 and coin.source = ?3 " +
            "and coin.time >= ?4 and coin.time<?5")
    Integer findByNewUserRechargeAndCoinTypeAndCoinSourceAndTime(List<Long> userIds, CoinType CoinType,
                                                                 CoinSource coinSource, Date startTime, Date endTime);

    /**
     * 根据用户id和钱币类型查找钱币流动详情
     * @param userId 用户id
     * @param coinType 钱币类型
     * @return 钱币流动详情
     */
    @Query("select coin from CoinBean coin where coin.userId = ?1 and coin.coinType = ?2")
    Page<CoinBean> findByUserIdAndCoinType(Long userId, CoinType coinType, Pageable pageable);


    /**
     * 查询推广赠送钱币总数
     * @param userId 用户id
     * @return 钱币总数
     */
    @Query("select sum (coin.number) from CoinBean coin where coin.userId = ?1 and coin.promotion = true ")
    Integer findReward(Long userId);

    /**
     * 查询推广赠送钱币总数
     * @param userId 用户id
     * @return 钱币总数
     */
    @Query("select sum (coin.number) from CoinBean coin where coin.userId = ?1 and coin.promotion = true " +
            "and coin.time >= ?2 and coin.time < ?3 ")
    Integer findReward(Long userId, Date stateTime, Date endTime);

    /**
     * 查询推广赠送钱币流动详情
     * @param userId 用户id
     * @return 钱币流动详情
     */
    @Query("select coin from CoinBean coin where coin.userId = ?1 and coin.promotion = true ")
    Page<CoinBean> findReward(Long userId, Pageable pageable);

    /**
     * 获取用户金钱总数
     * @param coinType 钱币类型
     * @return 总数
     */
    @Query("select sum(coin.number) from CoinBean coin inner join UserMessage  userMessage " +
            "on userMessage.id = coin.userId where coin.coinType = ?1 and userMessage.robot = false ")
    Integer getTotal(CoinType coinType);

    @Modifying
    @Query("delete from CoinBean coin where coin.place = ?1")
    void deleteByPlace(Long place);

}
