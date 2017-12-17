package com.mierro.fileOperation.po;

import com.mierro.fileOperation.common.UploadType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 上传文件<br>
 * 属性url 是相对于utils包的 {@link FileUtils}的rootURL的路径
 * @author 唐亮
 * @version 2016年6月20日
 * @see UploadFile
 * @since
 */
@Entity
@Table(name = "t_upload_file")
public class UploadFile implements Serializable{

    /** 上传ID */
    @Id
    @GeneratedValue(generator = "id")
    @GenericGenerator(name= "id",strategy = "com.mierro.common.common.KeyGenerator")
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    /** 服务器上的资源路径 */
    @Column(unique = true)
    private String url;
    /** 客户端发送过来的文件名 */
    private String fileName;
    /** 上传文件类型 */
    private UploadType type;
    /** 客户端发送过来的MIME类型 */
    private String contentType;
    /** 使用中 */
    private boolean isUsing;
    /** 上一次文件ID */
    private Long previousId;
    /** 是否删除 */
    private boolean isDelete;
    /** 创建时间 */
    private Date createTime;

    public UploadFile() {}

    public UploadFile(String url, String fileName, UploadType type, String contentType) {
        this.url = url;
        this.fileName = fileName;
        this.type = type;
        this.contentType = contentType;
    }


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "url")
    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Column(name = "fileName")
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    public UploadType getType() {
        return this.type;
    }

    public void setType(UploadType type) {
        this.type = type;
    }

    @Column(name = "contentType", nullable = false)
    public String getContentType() {
        return contentType;
    }
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Column(name = "isUsing")
    @Type(type = "boolean")
    public boolean getUsing() {
        return isUsing;
    }
    public void setUsing(boolean using) {
        this.isUsing = using;
    }

    @Column(name = "isDelete")
    @Type(type = "boolean")
    public boolean getIsDelete() {
        return this.isDelete;
    }
    public void setIsDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createTime", length = 19)
    @CreationTimestamp
    public Date getCreateTime() {
        return this.createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getPreviousId() {
        return previousId;
    }
    public void setPreviousId(Long previousId) {
        this.previousId = previousId;
    }
}
