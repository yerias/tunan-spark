package com.tunna.spark.presto.udf;


import com.facebook.presto.spi.Plugin;
import com.google.common.collect.ImmutableSet;
import com.tunna.spark.presto.aggudf.TunanAggregationUDF;

import java.util.Set;

    public class ProstoUDFPlugin implements Plugin {
        @Override
        public Set<Class<?>> getFunctions() {
            return ImmutableSet.<Class<?>>builder()
                    .add(PrefixUDF.class)
                    .add(TunanAggregationUDF.class) // 新加的
                    .build();
        }
    }