/*
 * Copyright 2020-2021 RW-HPS Team and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/RW-HPS/RW-HPS/blob/master/LICENSE
 */

package com.github.dr.webapi.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public abstract class ReExp {

    private int retryfreq = 0;

    // 重试的睡眠时间
    private int sleepTime = 0;

    private boolean isException = true;

    private Class exception = Exception.class;

    private final Map<Class, String> ClassExpResult = new ConcurrentHashMap<Class, String>();

    public ReExp setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
        return this;
    }

    public ReExp setRetryFreq(int retryfreq) {
        this.retryfreq = retryfreq;
        return this;
    }

    public ReExp setException(Class exception) {
        this.isException = true;
        this.exception = exception;
        return this;
    }

    public ReExp addException(Class exception,String result) {
        ClassExpResult.put(exception,result);
        return this;
    }

    /**
     * 重试
     * @return
     */
    protected abstract Object runs() throws Exception;

    protected abstract Data defruns();


    public Object execute() {
        for (int i = 0; i < retryfreq; i++) {
            try {
                return runs();
            } catch (Exception e) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException interruptedException) {
                }
            }
        }
        return defruns();
    }

    public Data countExecute(String name) {
        Data data = new Data(name,retryfreq);
        for (int i = 0; i < retryfreq; i++) {
            try {
                data.result = runs();
                return data;
            } catch (Exception e) {
                if (isException && exception.isInstance(e)) {
                    data.failures++;
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException interruptedException) {
                    }
                } else {
                    Iterator it = ClassExpResult.entrySet().iterator();
                    while(it.hasNext()){
                        Entry entry = (Entry)it.next();
                        Class classdata = (Class) entry.getKey();
                        if (classdata.isInstance(e)) {
                            data.result = entry.getValue();
                            return data;
                        }
                    }
                    return defruns();
                }
            }
        }
        return defruns();
    }

    public static class Data {
        public Object result = null;
        public int cout = 0;
        public int failures = 0;
        public Data(String name,int rq) {
            cout = rq;
        }
    }

}