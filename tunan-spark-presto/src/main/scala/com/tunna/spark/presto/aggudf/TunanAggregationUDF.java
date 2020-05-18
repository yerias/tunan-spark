package com.tunna.spark.presto.aggudf;


import com.facebook.presto.spi.block.BlockBuilder;
import com.facebook.presto.spi.function.*;
import com.facebook.presto.spi.type.StandardTypes;
import io.airlift.slice.Slice;
import io.airlift.slice.Slices;

import static com.facebook.presto.spi.type.VarcharType.VARCHAR;

@AggregationFunction("tunan_concat")    // Agg方法名
public class TunanAggregationUDF {

    @InputFunction  //输入函数
    public static void input(StringValueState state,@SqlType(StandardTypes.VARCHAR) Slice value){

        state.setStringValue(Slices.utf8Slice(isNull(state.getStringValue())+"|"+value.toStringUtf8()));
    }

    @CombineFunction    //合并函数
    public static void combine(StringValueState state1,StringValueState state2){
        state1.setStringValue(Slices.utf8Slice(isNull(state1.getStringValue())+"|"+isNull(state2.getStringValue())));
    }

    @OutputFunction(StandardTypes.VARCHAR)  //输出函数
    public static void output(StringValueState state, BlockBuilder builder){
        VARCHAR.writeSlice(builder,state.getStringValue());
    }


    // 判断null值
    public static String isNull(Slice slice){
        return slice ==null?"":slice.toStringUtf8();
    }
}
