package com.mierro.main.Listener;

import com.mierro.authority.dao.ConfigDao;
import com.mierro.authority.entity.ConfigBean;
import com.mierro.common.common.FileUtils;
import com.mierro.common.common.SpringTool;
import com.mierro.fileOperation.common.UploadType;
import com.mierro.fileOperation.po.UploadFile;
import com.mierro.fileOperation.service.IUploadService;
import com.mierro.main.Listener.Thread.ItemThread;
import com.mierro.main.Listener.task.Task;
import com.mierro.main.common.ConfigSetting;
import com.mierro.main.entity.ItemBean;
import com.mierro.main.global.ItemCache;
import com.mierro.main.global.SystemCache;
import com.mierro.main.service.ItemService;
import com.mierro.main.service.SystemService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by 黄晓滨 simba on 2017/8/8.
 * Remarks：系统初始化
 */
@WebListener
public class InitializationListener implements ServletContextListener {

    private Integer mark= 1;

    @Override
    @Transactional
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        WebApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(servletContextEvent.getServletContext());
        ItemService itemService = (ItemService)springContext.getBean("ItemService");
        ConfigDao configDao = (ConfigDao) SpringTool.getBeanByClass(ConfigDao.class);
        if (mark == 1){
            System.out.println("/***************************系统初始化******************************/");
            System.out.println("/**************************初始化配置表******************************/");
            if (configDao.findByKeyId(ConfigSetting.one_day) == null){
                ConfigBean configBean = new ConfigBean();
                configBean.setKeyId(ConfigSetting.one_day);
                configBean.setMessage("用户第1天签到增加积分");
                configBean.setValue("10");
                configDao.save(configBean);
            }
            if(configDao.findByKeyId(ConfigSetting.two_day) == null){
                ConfigBean configBean2 = new ConfigBean();
                configBean2.setKeyId(ConfigSetting.two_day);
                configBean2.setMessage("用户第2天签到增加积分");
                configBean2.setValue("10");
                configDao.save(configBean2);
            }
            if(configDao.findByKeyId(ConfigSetting.three_day) == null){
                ConfigBean configBean3 = new ConfigBean();
                configBean3.setKeyId(ConfigSetting.three_day);
                configBean3.setMessage("用户第3天签到增加积分");
                configBean3.setValue("20");
                configDao.save(configBean3);
            }
            if(configDao.findByKeyId(ConfigSetting.four_day) == null){
                ConfigBean configBean4 = new ConfigBean();
                configBean4.setKeyId(ConfigSetting.four_day);
                configBean4.setMessage("用户第4天签到增加积分");
                configBean4.setValue("30");
                configDao.save(configBean4);
            }
            if(configDao.findByKeyId(ConfigSetting.five_day) == null){
                ConfigBean configBean5 = new ConfigBean();
                configBean5.setKeyId(ConfigSetting.five_day);
                configBean5.setMessage("用户第5天签到增加积分");
                configBean5.setValue("30");
                configDao.save(configBean5);
            }
            if(configDao.findByKeyId(ConfigSetting.six_day) == null){
                ConfigBean configBean6 = new ConfigBean();
                configBean6.setKeyId(ConfigSetting.six_day);
                configBean6.setMessage("用户第6天签到增加积分");
                configBean6.setValue("50");
                configDao.save(configBean6);
            }
            if(configDao.findByKeyId(ConfigSetting.seven_day) == null){
                ConfigBean configBean7 = new ConfigBean();
                configBean7.setKeyId(ConfigSetting.seven_day);
                configBean7.setMessage("用户第7天签到增加积分");
                configBean7.setValue("50");
                configDao.save(configBean7);
            }

            if(configDao.findByKeyId(ConfigSetting.fixed_chest) == null){
                ConfigBean configBean8 = new ConfigBean();
                configBean8.setKeyId(ConfigSetting.fixed_chest);
                configBean8.setMessage("固定宝箱设置(默认连续签到4天获取7天获取宝箱)");
                configBean8.setValue("[{\"integral\":50,\"number\":4},{\"integral\":100,\"number\":7}]");
                configDao.save(configBean8);
            }

            if(configDao.findByKeyId(ConfigSetting.map_key) == null){
                ConfigBean configBean8 = new ConfigBean();
                configBean8.setKeyId(ConfigSetting.map_key);
                configBean8.setMessage("高德地图key");
                configBean8.setValue("");
                configDao.save(configBean8);
            }

            if(configDao.findByKeyId(ConfigSetting.recharge) == null){
                ConfigBean configBean9 = new ConfigBean();
                configBean9.setKeyId(ConfigSetting.recharge);
                configBean9.setMessage("充值展示标志");
                configBean9.setValue("true");
                configDao.save(configBean9);
            }

            if(configDao.findByKeyId(ConfigSetting.gift_coin) == null){
                ConfigBean configBean11 = new ConfigBean();
                configBean11.setKeyId(ConfigSetting.gift_coin);
                configBean11.setMessage("注册赠送拍币数");
                configBean11.setValue("0");
                configDao.save(configBean11);
            }


            System.out.println("/************************加载高德地图key*****************************/");

            ConfigBean configBean = configDao.findByKeyId(ConfigSetting.map_key);
            SystemCache.mapkey = configBean.getValue();

            IUploadService uploadService = (IUploadService) SpringTool.getBeanByClass(IUploadService.class);
            if (uploadService.findOne(1L) == null){
                System.out.println("/*************************加载默认头像*******************************/");
                // 获取文件地址
                for (int i=1 ; i < 17; i++){
                    File file = FileUtils.getConfigFile("./avatar/"+i+".png");
                    if (file == null){
                        System.out.println("/************************加载默认头像失败*****************************/");
                    }else {
                        UploadType uploadType = UploadType.IMAGE;
                        UploadFile uploadVo = new UploadFile();
                        uploadVo.setId((long) i);
                        uploadVo.setContentType("image/jpeg");
                        uploadVo.setType(uploadType);
                        uploadVo.setUrl(uploadType.getRelativeURL(i+".png"));
                        uploadVo.setFileName(i+".png");
                        uploadVo = uploadService.save(uploadVo);
                        InputStream in;
                        try {
                            in = new FileInputStream(file);
                            FileUtils.create(in, FileUtils.getResourcePath(uploadVo.getUrl()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            System.out.println("/************************删除缓存临时记录******************************/");

            SystemService systemService = (SystemService) SpringTool.getBeanByClass(SystemService.class);
            systemService.deleteCache();

            System.out.println("/*************************用户签到记录重置******************************/");
            Task task = new Task();
            task.reportedReset();
            System.out.println("/**************************进行商品装载******************************/");
            try {
               List<ItemBean> itemBeans =  itemService.findAll(false);
               if (!ItemCache.load(itemBeans)){
                   System.out.println("商品缓存发生装载错误");
                   this.contextDestroyed(servletContextEvent);
               }
            } catch (IOException e) {
                System.out.println("转载商品时发生数据转换异常");
                this.contextDestroyed(servletContextEvent);
            }
            //证明进入初始化函数(保证只开启一次)
            if (mark == 1) {
                ItemThread thread = new ItemThread(ItemThread.class.getName());
                thread.start();
                mark = 2;
            }
            System.out.println("/*************************系统初始化完成*****************************/");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("/*************************系统销毁****************************/");
    }
}
