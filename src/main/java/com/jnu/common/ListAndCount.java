package com.jnu.common;

import java.util.List;

/**
 * @author ：Killer
 * @date ：Created in 20-6-11 下午5:24
 * @description：${description}
 * @modified By：
 * @version: version
 */
public class ListAndCount<T> {
    private List<T> list;
    private long count;

    public ListAndCount() {
    }

    public ListAndCount(List<T> list, long count) {
        this.list = list;
        this.count = count;
    }

    public List<T> getList() {
        return this.list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public long getCount() {
        return this.count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}