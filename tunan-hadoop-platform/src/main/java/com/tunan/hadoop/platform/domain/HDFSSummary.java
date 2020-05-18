package com.tunan.hadoop.platform.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "hdfs_summary")
public class HDFSSummary extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;    //主键
    private long dfsTotal; //总容量
    private long dfsUsed; //使用容量
    private float percentUsed; //使用容量百分比
    private long dfsFree; //空闲容量
    private float freePercentUsed; //空闲容量百分比
    private long blocksTotal;    //总的block数量
    private long filesTotal;     //总的文件数量
    private long missingBlocks; //丢失的块的数量

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDfsTotal() {
        return dfsTotal;
    }

    public void setDfsTotal(long dfsTotal) {
        this.dfsTotal = dfsTotal;
    }

    public long getDfsUsed() {
        return dfsUsed;
    }

    public void setDfsUsed(long dfsUsed) {
        this.dfsUsed = dfsUsed;
    }

    public float getPercentUsed() {
        return percentUsed;
    }

    public void setPercentUsed(float percentUsed) {
        this.percentUsed = percentUsed;
    }

    public long getDfsFree() {
        return dfsFree;
    }

    public void setDfsFree(long dfsFree) {
        this.dfsFree = dfsFree;
    }

    public float getFreePercentUsed() {
        return freePercentUsed;
    }

    public void setFreePercentUsed(float freePercentUsed) {
        this.freePercentUsed = freePercentUsed;
    }

    public long getBlocksTotal() {
        return blocksTotal;
    }

    public void setBlocksTotal(long blocksTotal) {
        this.blocksTotal = blocksTotal;
    }

    public long getFilesTotal() {
        return filesTotal;
    }

    public void setFilesTotal(long filesTotal) {
        this.filesTotal = filesTotal;
    }

    public long getMissingBlocks() {
        return missingBlocks;
    }

    public void setMissingBlocks(long missingBlocks) {
        this.missingBlocks = missingBlocks;
    }

    @Override
    public String toString() {
        return "HDFSSummary{" +
                "id=" + id +
                ", dfsTotal=" + dfsTotal +
                ", dfsUsed=" + dfsUsed +
                ", percentUsed=" + percentUsed +
                ", dfsFree=" + dfsFree +
                ", freePercentUsed=" + freePercentUsed +
                ", blocksTotal=" + blocksTotal +
                ", filesTotal=" + filesTotal +
                ", missingBlocks=" + missingBlocks +
                '}';
    }
}
