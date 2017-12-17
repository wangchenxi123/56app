package com.mierro.fileOperation.service;

import com.mierro.common.common.ResponseCode;
import com.mierro.fileOperation.common.Upload;
import com.mierro.fileOperation.po.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.Field;
import java.util.stream.Stream;

/**
 * 文件上传,查看 ,下载管理业务
 *
 * @author 唐亮
 * @version 1.0
 */
public interface IUploadService extends JpaRepository<UploadFile, Long> {


    /**
     * 连接上一次文件到现在的文件上
     *
     * @param nowId
     * @param previousId
     */
    default void linkPreviousFile(Long nowId, Long previousId) {
        UploadFile now = ResponseCode.business.notNull(getOne(nowId), "找不到文件ID");
        UploadFile previous = ResponseCode.business.notNull(getOne(previousId), "找不到文件ID");
        now.setPreviousId(previousId);
        save(now);
        previous.setUsing(false);
        save(previous);
    }

    /**
     * 设置使用状态
     *
     * @param fileId
     * @return
     */
    default UploadFile setUsing(Long fileId) {
        UploadFile now = ResponseCode.business.notNull(getOne(fileId), "找不到文件");
        now.setUsing(true);
        return save(now);
    }

    /**
     * @param vo  新的数据
     * @param po  数据库已存储的数据
     * @param <T>
     * @param <S>
     */
    default <T, S> void linkPreviousFile(T vo, S po) {
        Class<?> voClass = vo.getClass();
        getUploadFields(po)
                .forEach(field -> {
                    try {
                        String fieldName = field.getName();
                        Field vofield = voClass.getDeclaredField(fieldName);
                        vofield.setAccessible(true); //取消Java语言访问检查
                        if (!vofield.getType().equals(Long.class))
                            return;
                        Long nowId = (Long) vofield.get(vo);
                        Long previousId = (Long) field.get(po);

                        if (nowId != null && previousId != null)
                            linkPreviousFile(nowId, previousId);
                        else if (nowId != null && previousId == null) {
                            setUsing(nowId);
                        }
                    } catch (IllegalAccessException | NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                });
    }

    default <S> void setUsing(S po) {
        getUploadFields(po)
                .forEach(field -> {
                    try {
                        Long fileId = (Long) field.get(po);
                        if (fileId != null) {
                            setUsing(fileId);
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });
    }

    default <T> Stream<Field> getUploadFields(final T po) {
        return Stream.of(po.getClass().getDeclaredFields())
                .filter(field -> field.getAnnotation(Upload.class) != null)
                .filter(field -> field.getType().equals(Long.class))
                .peek(field -> field.setAccessible(true));//取消Java语言访问检查
    }
}
