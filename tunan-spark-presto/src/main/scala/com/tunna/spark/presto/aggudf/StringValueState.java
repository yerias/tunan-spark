package com.tunna.spark.presto.aggudf;


import com.facebook.presto.spi.function.AccumulatorState;
import io.airlift.slice.Slice;

public interface StringValueState extends AccumulatorState {
    Slice getStringValue();
    void setStringValue(Slice value);
}


