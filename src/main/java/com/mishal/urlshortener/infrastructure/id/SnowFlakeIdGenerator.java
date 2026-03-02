package com.mishal.urlshortener.infrastructure.id;

public class SnowFlakeIdGenerator {

    private final long machineId;
    private long sequence = 0;
    private long lastTimestamp = -1;

    public SnowFlakeIdGenerator(long machineId){
        this.machineId = machineId;
    }

    public synchronized long nextId(){
        long timestamp = System.currentTimeMillis();
        if(timestamp==lastTimestamp){
            sequence++;
        }else{
            sequence = 0;
        }

        lastTimestamp = timestamp;

        return (timestamp<<12)|(machineId<<5)|sequence;
    }
}
