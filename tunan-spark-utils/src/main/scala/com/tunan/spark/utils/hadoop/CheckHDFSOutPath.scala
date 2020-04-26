package com.tunan.spark.utils.hadoop

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path

object CheckHDFSOutPath {

    def ifExistsDeletePath(conf:Configuration,out:String): Unit ={
        var fs: FileSystem = null
        try {
             fs = FileSystem.get(conf)
            val path = new Path(out)
            if (fs.exists(path)) {
                fs.delete(path, true)
            }
        } finally{
           /* if (fs!=null){
                fs.close()
            }*/
        }
    }
}
