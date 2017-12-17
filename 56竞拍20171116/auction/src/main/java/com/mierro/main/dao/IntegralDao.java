package com.mierro.main.dao;

import com.mierro.authority.entity.UserMessage;
import com.mierro.main.common.OperationalType;
import com.mierro.main.entity.IntegralBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * Created by 黄晓滨 simba on 2017/7/29.
 * Remarks：
 */
public interface IntegralDao extends JpaRepository<IntegralBean,Long> {

    /**
     * 查询某一个用户积分总数
     * @param userId 用户id
     * @return 积分总数
     */
    @Query("select sum (integer.number) from IntegralBean integer where integer.userId = ?1")
    Integer findByUserId(Long userId);

    /**
     * 查询某一个用户积分总数
     * @param userId 用户id
     * @return 积分总数
     */
    @Query("select max (integer.time) from IntegralBean integer where integer.userId = ?1 and integer.operationalType = ?2")
    Date findLatest(Long userId, OperationalType operationalType);

    /**
     * 查询某一个用户积分总数
     * @param userId 用户id
     * @return 积分总数
     */
    @Query("select integer from IntegralBean integer where integer.id = " +
            "(select max(integer.id) from IntegralBean integer " +
            "where integer.userId = ?1 and integer.operationalType = ?2 )")
    IntegralBean findOne(Long userId, OperationalType operationalType);

    /**
     * 查询某一个用户积分流动详情
     * @param userId 用户id
     * @param pageable 分页参数
     * @return 积分流动详情
     */
    @Query("select integer from IntegralBean integer where integer.userId = ?1")
    Page<IntegralBean> findByUserId(Long userId, Pageable pageable);

    /**
     * 查询所有用户（包括最近签到时间）
     * @return 用户集合
     */
    @Query("select new UserMessage(usermessage.id, max(inte.time)) from UserMessage usermessage " +
            "inner join IntegralBean inte on inte.userId = usermessage.id where usermessage.robot = false " +
            "GROUP BY inte.userId")
    List<UserMessage> reportedReset();

    /**
     * 用户所拥有的积分数
     * @return 积分总数
     */
    @Query("select sum(integral.number) from IntegralBean integral")
    Integer getTotal();
}
