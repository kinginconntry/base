package com.needto.quartz.model;

import com.needto.tool.entity.Dict;

public class QuartzSimpleTrigger extends QuartzTrigger {
    public int repeatCount;
    public int repatInterval;
    public int timesTriggered;

    public static QuartzSimpleTrigger builder(Dict dict){
        return null;
    }

    public int getRepeatCount() {
        return repeatCount;
    }

    public void setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
    }

    public int getRepatInterval() {
        return repatInterval;
    }

    public void setRepatInterval(int repatInterval) {
        this.repatInterval = repatInterval;
    }

    public int getTimesTriggered() {
        return timesTriggered;
    }

    public void setTimesTriggered(int timesTriggered) {
        this.timesTriggered = timesTriggered;
    }
}
