package com.mierro.common.common;

import java.io.*;
import java.util.zip.*;

/**
 * Created by 黄晓滨 simba on 2017/8/9.
 * Remarks：
 */
public class ZipUtils {

    /**
     * 缓存区大小默认20480
     */
    private final static int FILE_BUFFER_SIZE = 20480;

    /**
     * 将指定目录的ZIP压缩文件解压到指定的目录
     * @param zipFilePath       ZIP压缩文件的路径
     * @param zipFileName       ZIP压缩文件名字
     * @param targetFileDir     ZIP压缩文件要解压到的目录
     * @return flag             布尔返回值
     */
    public static boolean unzip(String zipFilePath, String zipFileName, String targetFileDir){
        boolean flag ;
        //1.判断压缩文件是否存在，以及里面的内容是否为空
        File file;           //压缩文件(带路径)
        ZipFile zipFile ;
        file = new File(zipFilePath + "/" + zipFileName);
        System.out.println(">>>>>>解压文件【" + zipFilePath + "/" + zipFileName + "】到【" + targetFileDir + "】目录下<<<<<<");
        if(false == file.exists()) {
            System.out.println(">>>>>>压缩文件【" + zipFilePath + "/" + zipFileName + "】不存在<<<<<<");
            return false;
        } else if(0 == file.length()) {
            System.out.println(">>>>>>压缩文件【" + zipFilePath + "/" + zipFileName + "】大小为0不需要解压<<<<<<");
            return false;
        } else {
            //2.开始解压ZIP压缩文件的处理
            byte[] buf = new byte[FILE_BUFFER_SIZE];
            int readSize;
            ZipInputStream zis = null;
            FileOutputStream fos = null;
            try {
                // 检查是否是zip文件
                zipFile = new ZipFile(file);
                zipFile.close();
                // 判断目标目录是否存在，不存在则创建
                File newdir = new File(targetFileDir);
                if (false == newdir.exists()) {
                    newdir.mkdirs();
                }
                zis = new ZipInputStream(new FileInputStream(file));
                ZipEntry zipEntry = zis.getNextEntry();
                // 开始对压缩包内文件进行处理
                while (null != zipEntry) {
                    String zipEntryName = zipEntry.getName().replace('\\', '/');
                    //判断zipEntry是否为目录，如果是，则创建
                    if(zipEntry.isDirectory()) {
                        int indexNumber = zipEntryName.lastIndexOf('/');
                        File entryDirs = new File(targetFileDir + "/" + zipEntryName.substring(0, indexNumber));
                        entryDirs.mkdirs();
                    } else {
                        try {
                            fos = new FileOutputStream(targetFileDir + "/" + zipEntryName);
                            while ((readSize = zis.read(buf, 0, FILE_BUFFER_SIZE)) != -1) {
                                fos.write(buf, 0, readSize);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            throw new RuntimeException(e.getCause());
                        } finally {
                            try {
                                if (null != fos) {
                                    fos.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                throw new RuntimeException(e.getCause());
                            }
                        }
                    }
                    zipEntry = zis.getNextEntry();
                }
                flag = true;
            } catch (ZipException e) {
                e.printStackTrace();
                throw new RuntimeException(e.getCause());
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e.getCause());
            } finally {
                try {
                    if (null != zis) {
                        zis.close();
                    }
                    if (null != fos) {
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e.getCause());
                }
            }
        }
        return flag;
    }

    /**
     * 解压文件
     * @param filePath 压缩文件路径
     */
    public static void unzip(String filePath) {
        File source = new File(filePath);
        if (source.exists()) {
            ZipInputStream zis = null;
            BufferedOutputStream bos = null;
            try {
                zis = new ZipInputStream(new FileInputStream(source));
                ZipEntry entry ;
                while ((entry = zis.getNextEntry()) != null) {
                    if(entry.isDirectory()){
                        continue;
                    }
                    File target = new File(source.getParent(), entry.getName());
                    if (!target.getParentFile().exists()) {
                        // 创建文件父目录
                        target.getParentFile().mkdirs();
                    }
                    // 写入文件
                    bos = new BufferedOutputStream(new FileOutputStream(target));
                    int read ;
                    byte[] buffer = new byte[FILE_BUFFER_SIZE];
                    while ((read = zis.read(buffer, 0, buffer.length)) != -1) {
                        bos.write(buffer, 0, read);
                    }
                    bos.flush();
                }
                zis.closeEntry();
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
            finally {
                IOUtil.closeQuietly(zis, bos);
            }
        }
    }

    /**
     * 将filePaths 文件集合打包到zipPath 这个zip文件中
     * @param zipPath 压缩后的文件路径
     * @param filePaths 需要压缩的文件路径列表
     *
     */
    public static void filesToZip(String zipPath,String[] filePaths)
    {
        File target = new File(zipPath);
        // 压缩文件名=源文件名.zip
        if (target.exists())
        {
            target.delete(); // 删除旧的文件
        }
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        try
        {
            fos = new FileOutputStream(target);
            zos = new ZipOutputStream(new BufferedOutputStream(fos));
            // 添加对应的文件Entry
            addEntry(filePaths, zos);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            IOUtil.closeQuietly(zos, fos);
        }
    }

    /**
     * 扫描添加文件Entry
     * @param filePaths 文件路径数组
     * @param zos zip流
     * @throws IOException
     */
    private static void addEntry(String[] filePaths, ZipOutputStream zos)
            throws IOException {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        File tempFile ;
        try {
            for (String filePath : filePaths) {
                tempFile = new File(filePath);
                fis = new FileInputStream(tempFile);
                byte[] buffer = new byte[FILE_BUFFER_SIZE];
                bis = new BufferedInputStream(fis, buffer.length);
                int read = 0;
                zos.putNextEntry(new ZipEntry(tempFile.getName()));
                while ((read = bis.read(buffer, 0, buffer.length)) != -1) {
                    zos.write(buffer, 0, read);
                }
                zos.closeEntry();
            }
        }
        finally {
            IOUtil.closeQuietly(bis, fis);
        }
    }

    /**
     * 用于关闭流对象
     * @author  wanglei
     * @version  [版本号, 2016年6月1日]
     * @see  [相关类/方法]
     * @since  [产品/模块版本]
     */
    static class IOUtil {
        /**
         * 关闭一个或多个流对象
         *
         * @param closeables 可关闭的流对象列表
         * @throws IOException
         */
        public static void close(Closeable... closeables) throws IOException {
            if (closeables != null) {
                for (Closeable closeable : closeables) {
                    if (closeable != null) {
                        closeable.close();
                    }
                }
            }
        }

        /**
         * 关闭一个或多个流对象
         *
         * @param closeables 可关闭的流对象列表
         */
        public static void closeQuietly(Closeable... closeables) {
            try {
                close(closeables);
            } catch (IOException e) {
            }
        }
    }

    public static void main(String[] args) {
        String zipFilePath = "C:\\home";
        String zipFileName = "lp20120301.zip";
        String targetFileDir = "C:\\home\\lp20120301";
        boolean flag = ZipUtils.unzip(zipFilePath, zipFileName, targetFileDir);
        if(flag) {
            System.out.println(">>>>>>解压成功<<<<<<");
        } else {
            System.out.println(">>>>>>解压失败<<<<<<");
        }
    }
}
