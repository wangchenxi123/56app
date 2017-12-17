package com.mierro.robot.service.impl;

import com.mierro.common.common.ResponseCode;
import com.mierro.main.entity.ItemBean;
import com.mierro.robot.dao.RobotItemDao;
import com.mierro.robot.entity.RobotItemBean;
import com.mierro.robot.service.RobotItemService;
import com.mierro.robot.service.RobotService;
import com.mierro.robot.utils.Tool;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 机器人商品服务
 * Created by tlseek on 2017/8/22.
 */
@Service("robotItemService")
public class RobotItemServiceImpl implements RobotItemService {

    @Resource
    private RobotItemDao robotItemDao;
    @Resource
    RobotService robotService;

    @Override
    public void loading() {
        // 加载
        List<RobotItemBean> itemList = robotItemDao.findItemByRobotItemIsNull().stream()
                .map(this::createRobotItemBean).collect(Collectors.toList());

        if (!itemList.isEmpty()) {
            robotItemDao.save(itemList);
        }

    }


    @Override
    public Page<RobotItemBean> find(String name, Integer pageNo, Integer pageSize) {

        loading();

        Page<RobotItemBean> page;
        pageNo = pageNo <= 1 ? 0 : pageNo-1;
        name = name == null ? "%" : "%" + name + "%";
        page =  robotItemDao.findByQuery(name, new PageRequest(pageNo, pageSize));
        return page;
    }

    @Transactional
    @Override
    public void update(RobotItemBean robotItem) {
        boolean min = robotItem.getControlLineMin() != null , max = robotItem.getControlLineMax() != null;
        RobotItemBean _robotItemBean = ResponseCode.business.notNull(robotItemDao.findOne(robotItem.getId()), "找不到机器人商品");
        boolean update = false;
        if (min && max) {
            ResponseCode.business.assertTrue(robotItem.getControlLineMin() < robotItem.getControlLineMax(),"controlLineMin 不能大于 controlLineMax");
            _robotItemBean.setControlLineMin(robotItem.getControlLineMin());
            _robotItemBean.setControlLineMax(robotItem.getControlLineMax());
            update = true;
        } else if (min || max) {
            ResponseCode.business.failure("controlLineMin, controlLineMax要同时存在");
        }

        if (robotItem.getCrossingAuctionRate() != null) {
            _robotItemBean.setCrossingAuctionRate(robotItem.getCrossingAuctionRate());
        }
        robotItemDao.save(_robotItemBean);
        if (update) {
            robotService.updateRecord(_robotItemBean.getItemId());
        }
    }

    @Transactional
    @Override
    public RobotItemBean getRobotItemBean(ItemBean item) {
        return Tool.getOrDefault(robotItemDao.findByItemId(item.getId()), () -> createRobotItemBean(item));
    }

    /**
     * 根据item创建机器人商品bean
     * @param item
     * @return
     */
    private RobotItemBean createRobotItemBean(ItemBean item) {
        Integer baseLine = item.getControl_line();
        if (baseLine <= 1) {
            return new RobotItemBean(item.getId(), 1, 2, 95);
        } else if (baseLine <= 2) {
            return new RobotItemBean(item.getId(), 1, 3, 95);
        } else {
            int halfRange = baseLine * 1 / 3;
            return new RobotItemBean(item.getId(), baseLine - halfRange, baseLine + halfRange, 95);
        }
    }
}
