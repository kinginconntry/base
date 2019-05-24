package com.needto.quartz.model;

public class QuartzCronTrigger extends QuartzTrigger {
    public String cron;

    public static QuartzCronTrigger builder(Dict dict){
        return null;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }
}
