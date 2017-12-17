package com.mierro.robot.utils;

import com.mierro.common.common.FileUtils;

import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by tlseek on 2017/10/20.
 */
public class Log {


    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    static DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS");

    StringBuffer itemBuffer = new StringBuffer();


    /**
     * 追加文件：使用FileWriter
     *
     * @param content
     */
    synchronized public void buffer(Object content) {
        itemBuffer.append(formatter2.format(LocalDateTime.now()));
        itemBuffer.append(" - ");
        itemBuffer.append(content);
        itemBuffer.append("\n");
    }
    /**
     * 追加文件：使用FileWriter
     *
     * @param content
     */
    synchronized public void buffer( Object... content) {
        itemBuffer.append(formatter2.format(LocalDateTime.now()));
        itemBuffer.append(" - ");
        for (Object s : content) {
            itemBuffer.append(s);
            itemBuffer.append(" ");
        }
        itemBuffer.append("\n");
    }

    public void write(Long itemId){
//        try {
//            String logPath = FileUtils.getRootURI().resolve("./WEB-INF/logs/robot/"+ itemId + "-"+formatter.format(LocalDate.now())+".log").getPath();
//            File file = new File(logPath);
//            if (!file.exists()) {
//                FileUtils.createFile(logPath);
//            }
//            FileWriter writer = new FileWriter(logPath, true);
//            writer.write(itemBuffer.toString());
//            writer.close();
//        } catch (URISyntaxException | IOException e) {
//            e.printStackTrace();
//        } finally {
//            itemBuffer = new StringBuffer();
//        }
    }
    public void write(){
//        try {
//            String logPath = FileUtils.getRootURI().resolve("./WEB-INF/logs/robot/"+formatter.format(LocalDate.now())+".log").getPath();
//            File file = new File(logPath);
//            if (!file.exists()) {
//                FileUtils.createFile(logPath);
//            }
//            FileWriter writer = new FileWriter(logPath, true);
//            writer.write(itemBuffer.toString());
//            writer.close();
//        } catch (URISyntaxException | IOException e) {
//            e.printStackTrace();
//        } finally {
//            itemBuffer = new StringBuffer();
//        }
    }

    public static void debug(Long itemId,String content) {
        try {
            String logPath = FileUtils.getRootURI().resolve("./WEB-INF/logs/robot/"+ itemId + "-"+formatter.format(LocalDate.now())+".log").getPath();
            method2(logPath, content);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    public static void debug(String content) {
        try {
            String logPath = FileUtils.getRootURI().resolve("./WEB-INF/logs/robot/"+formatter.format(LocalDate.now())+".log").getPath();
            method2(logPath, content);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    public static void debug(Long itemId,String... content) {
        try {
            String logPath = FileUtils.getRootURI().resolve("./WEB-INF/logs/robot/"+ itemId + "-"+formatter.format(LocalDate.now())+".log").getPath();
            method2(logPath, content);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    public static void debug(Object... content) {
        try {
            String logPath = FileUtils.getRootURI().resolve("./WEB-INF/logs/robot/"+formatter.format(LocalDate.now())+".log").getPath();
            method2(logPath, content);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


    /**
     * 追加文件：使用FileWriter
     *
     * @param fileName
     * @param content
     */
    private static void method2(String fileName, String content) {
//        try {
//            File file = new File(fileName);
//            if (!file.exists()) {
//                FileUtils.createFile(fileName);
//            }
//            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
//            FileWriter writer = new FileWriter(fileName, true);
//            writer.write(formatter2.format(LocalDateTime.now()));
//            writer.write(" - ");
//            writer.write(content);
//            writer.write("\n");
//            writer.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
    /**
     * 追加文件：使用FileWriter
     *
     * @param fileName
     * @param content
     */
    private static void method2(String fileName, Object[] content) {
//        try {
//            File file = new File(fileName);
//            if (!file.exists()) {
//                FileUtils.createFile(fileName);
//            }
//            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
//            FileWriter writer = new FileWriter(fileName, true);
//            writer.write(formatter2.format(LocalDateTime.now()));
//            writer.write(" - ");
//            for (Object s : content) {
//                writer.write(s.toString());
//                writer.write(" ");
//            }
//            writer.write("\n");
//            writer.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
