package com.tunan.hadoop.platform.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.MappedSuperclass;
import java.util.Date;


// 抽出共用的字段
@MappedSuperclass
public class BaseEntity {

    // 创建时间
    private Long createTime;

    // 是否删除
    @JsonIgnore
    private boolean trash;

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public boolean isTrash() {
        return trash;
    }

    public void setTrash(boolean trash) {
        this.trash = trash;
    }
}
