package com.tunna.spark.presto.udf;

import com.facebook.presto.spi.function.Description;
import com.facebook.presto.spi.function.ScalarFunction;
import com.facebook.presto.spi.function.SqlType;
import com.facebook.presto.spi.type.StandardTypes;
import io.airlift.slice.Slice;
import io.airlift.slice.Slices;

public class PrefixUDF {

    @Description("输入的数据加上前缀")       //描述
    @ScalarFunction("tunan_prefix")       //方法名称
    @SqlType(StandardTypes.VARCHAR)       //返回类型
    public static Slice prefix(@SqlType(StandardTypes.VARCHAR)Slice input){
        return Slices.utf8Slice("tunan_prefix_"+input.toStringUtf8());
    }
}
