package com.badeling.msbot.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Tuple2<T1, T2> {
    T1 t1;
    T2 t2;

    public static <T1, T2> Tuple2<T1, T2> of(T1 t1, T2 t2) {
        return new Tuple2<>(t1, t2);
    }

    public static <T1, T2> Tuple2<T1, T2> of() {
        return new Tuple2<>((T1) null, (T2) null);
    }
}
