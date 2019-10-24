package frc.team578.robot.utils;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class PIDFinished<T> {
    private long lastChecked = 0;
    private long checkIntervalMillis = 10;
    private int successCount = 0;
    private int stableCounts = 0;
    private boolean finished = false;
    private Predicate<T> successTest;
    private Supplier<T> supplyVal;
    private boolean lastTest;
    private T lastValue;

    public PIDFinished(long checkIntervalMillis, int stableCounts, Supplier<T> supplyVal, Predicate<T> successTest) {
        this.checkIntervalMillis = checkIntervalMillis;
        this.stableCounts = stableCounts;
        this.successTest = successTest;
        this.supplyVal = supplyVal;
    }

    public boolean isStable() {
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastChecked > checkIntervalMillis) {

            lastChecked = currentTime;

            lastValue = supplyVal.get();
            lastTest = successTest.test(lastValue);
            if (lastTest) {
                successCount++;
            } else {
                successCount = 0;
            }
        }

        finished = successCount >= stableCounts;

        return this.finished;
    }


    public boolean getFinished() {
        return this.finished;
    }

    @Override
    public String toString() {
        return "PIDFinished{" +
                "lastChecked=" + lastChecked +
                ", checkIntervalMillis=" + checkIntervalMillis +
                ", successCount=" + successCount +
                ", stableCounts=" + stableCounts +
                ", finished=" + finished +
                ", lastTest=" + lastTest +
                ", lastValue=" + lastValue +
                '}';
    }
}