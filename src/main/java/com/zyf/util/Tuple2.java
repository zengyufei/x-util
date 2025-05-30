package com.zyf.util;

import lombok.Data;

@Data
public class Tuple2<T1, T2> {
    T1 t1;
    T2 t2;

    public Tuple2(T1 t1, T2 t2) {
        this.t1 = t1;
        this.t2 = t2;
    }

    public Tuple2() {

    }
}
