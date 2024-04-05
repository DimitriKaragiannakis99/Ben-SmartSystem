package com.bensmartsystem.backend.model;

public class AlertTimer {
    private int delayInSeconds;
    private boolean isTimerActive;
    private House house;

    public AlertTimer(int delayInSeconds, House house) {
        this.delayInSeconds = delayInSeconds;
        this.isTimerActive = false;
        this.house = house;
    }

    public void startTimer() {
        if (!isTimerActive) {
            isTimerActive = true;
            new Thread(() -> {
                try {
                    Thread.sleep(delayInSeconds * 1000);
                    house.setPoliceCalled(true);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    isTimerActive = false;
                }
            }).start();
        }
    }

    public int getDelayInSeconds() {
        return delayInSeconds;
    }

    public void setDelayInSeconds(int delayInSeconds) {
        this.delayInSeconds = delayInSeconds;
    }
}
