package com.github.nitrico.lastadapter_sample.data;

import com.github.nitrico.lastadapter.StableId;

public class Car implements StableId {

    private final Long serialNumber;
    private final String model;

    public Car(Long serialNumber, String model) {
        this.serialNumber = serialNumber;
        this.model = model;
    }

    public Long getSerialNumber() {
        return serialNumber;
    }

    public String getModel() {
        return model;
    }

    @Override
    public long getStableId() {
        return serialNumber;
    }

}
