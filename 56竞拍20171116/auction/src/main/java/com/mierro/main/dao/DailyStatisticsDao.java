package com.mierro.main.dao;

import com.mierro.authority.entity.UserMessage;
import com.mierro.main.common.CoinSource;
import com.mierro.main.common.CoinType;
import com.mierro.main.common.OrderState;
import com.mierro.main.entity.DailyStatistics;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by 黄晓滨 simba on 2017/8/9.
 * Remarks：
 */
public interface DailyStatisticsDao extends JpaRepository<DailyStatistics,Long>{

    @Query("select usermessage from UserMessage usermessage where usermessage.robot = false")
    Page<UserMessage> findUserProfit(Pageable pageable);

    @Query("select SUM(sealed.cost)-SUM(sealed.market_price) FROM SealedBean sealed WHERE sealed.userId = ?1")
    Integer userReward(Long userId);

    @Query(value = "select new UserMessage (message,users.createTime) from CoinBean coin " +
            "INNER JOIN UserMessage message on message.id = coin.userId " +
            "INNER JOIN User users on users.id=message.id " +
            "WHERE message.robot = FALSE and coin.coinType = ?1 and coin.source = ?2 " +
            "GROUP BY coin.userId order by SUM(coin.number) desc ")
    Page<UserMessage> findUserSortTotalRechargeDesc(CoinType coinType, CoinSource coinSource, Pageable pageable);

    @Query(value = "select new UserMessage (message,users.createTime) from CoinBean coin " +
            "INNER JOIN UserMessage message on message.id = coin.userId " +
            "INNER JOIN User users on users.id=message.id " +
            "WHERE message.robot = FALSE and coin.coinType = ?1 and coin.source = ?2 " +
            "GROUP BY coin.userId order by SUM(coin.number) desc ")
    Page<UserMessage> findUserSortTotalRechargeAsc(CoinType coinType, CoinSource coinSource, Pageable pageable);

    @Query(value = "select new UserMessage (message ,users.createTime) from CoinBean coin " +
            "INNER JOIN UserMessage message on message.id = coin.userId " +
            "INNER JOIN User users on users.id=message.id " +
            "WHERE message.robot = FALSE and coin.coinType = ?1 " +
            "GROUP BY coin.userId order by SUM(coin.number) desc ")
    Page<UserMessage> findUserSortBalanceDesc(CoinType coinType, Pageable pageable);

    @Query(value = "select new UserMessage (message ,users.createTime) from CoinBean coin " +
            "INNER JOIN UserMessage message on message.id = coin.userId " +
            "INNER JOIN User users on users.id=message.id " +
            "WHERE message.robot = FALSE and coin.coinType = ?1 " +
            "GROUP BY coin.userId order by SUM(coin.number) ASC ")
    Page<UserMessage> findUserSortBalanceAsc(CoinType coinType, Pageable pageable);

    @Query("select new UserMessage (message ,users.createTime) from CoinBean coin  " +
            "INNER JOIN UserMessage message on message.id = coin.userId " +
            "INNER JOIN User users on users.id=message.id " +
            "left JOIN SealedBean sealed on sealed.userId = coin.userId " +
            "left JOIN OrderBean orders on orders.userId = coin.userId " +
            "WHERE message.robot = FALSE and coin.coinType = ?1 and coin.source = ?2 " +
            "GROUP BY coin.userId order by " +
            "SUM(coin.number)+ " +
            "SUM(case when orders.order_state!=?3 then orders.amounts else 0 end)- " +
            "SUM(case when orders.order_state!=?3 then sealed.market_price else 0 end) desc")
    Page<UserMessage> findUserSortIncomeDesc(CoinType coinType, CoinSource coinSource, OrderState orderState, Pageable pageable);

    @Query("select new UserMessage (message ,users.createTime) from CoinBean coin  " +
            "INNER JOIN UserMessage message on message.id = coin.userId " +
            "INNER JOIN User users on users.id=message.id " +
            "left JOIN SealedBean sealed on sealed.userId = coin.userId " +
            "left JOIN OrderBean orders on orders.userId = coin.userId " +
            "WHERE message.robot = FALSE and coin.coinType = ?1 and coin.source = ?2 " +
            "GROUP BY coin.userId order by " +
            "SUM(coin.number)+ " +
            "SUM(case when orders.order_state!=?3 then orders.amounts else 0 end)- " +
            "SUM(case when orders.order_state!=?3 then sealed.market_price else 0 end) desc")
    Page<UserMessage> findUserSortIncomeAsc(CoinType coinType, CoinSource coinSource, OrderState orderState, Pageable pageable);

}
