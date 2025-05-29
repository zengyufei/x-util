package com.zyf.util;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

@Data
@Accessors(chain = true)
public class Diff2<T, R> {

    private List<R> addList;
    private List<T> delList;
    // 已存在的对象集合 对应的新值
    private Map<T, R> updateMap;

    public List<Object> getEffectList() {
        final List<Object> list = new ArrayList<>(addList);
        list.addAll(getExistsList());
        return list;
    }

    public Diff2<T, R> addConsumer(BiConsumer<Diff2<T, R>, List<R>> biConsumer) {
        if (isNotEmpty(addList)) {
            biConsumer.accept(this, addList);
        }
        return this;
    }

    public Diff2<T, R> delConsumer(BiConsumer<Diff2<T, R>, List<T>> biConsumer) {
        if (isNotEmpty(delList)) {
            biConsumer.accept(this, delList);
        }
        return this;
    }

    public Diff2<T, R> existsConsumer(BiConsumer<Diff2<T, R>, List<T>> biConsumer) {
        final List<T> existsList = getExistsList();
        if (isNotEmpty(existsList)) {
            biConsumer.accept(this, existsList);
        }
        return this;
    }


    public Diff2<T, R> updateConsumer(BiConsumer<Diff2<T, R>, Map<T, R>> biConsumer) {
        if (updateMap != null && !updateMap.isEmpty()) {
            biConsumer.accept(this, updateMap);
        }
        return this;
    }

    public Diff2<T, R> forEachUpdateMapConsumer(BiConsumer<T, R> biConsumer) {
        if (updateMap != null && !updateMap.isEmpty()) {
            for (Map.Entry<T, R> entry : updateMap.entrySet()) {
                final T oldPo = entry.getKey();
                final R newPo = entry.getValue();
                biConsumer.accept(oldPo, newPo);
            }
        }
        return this;
    }

    private <X> boolean isNotEmpty(List<X> list) {
        return list != null && !list.isEmpty();
    }

    public List<T> getExistsList() {
        return getUpdateMapKeys();
    }

    public List<T> getUpdateMapKeys() {
        return updateMap.keySet().stream().toList();
    }

    public List<R> getUpdateMapValues() {
        return updateMap.values().stream().toList();
    }
}
