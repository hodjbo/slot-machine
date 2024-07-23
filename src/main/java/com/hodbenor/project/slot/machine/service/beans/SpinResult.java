package com.hodbenor.project.slot.machine.service.beans;

import com.hodbenor.project.slot.machine.rest.beans.BaseResult;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class SpinResult extends BaseResult {
    private final int pointBalance;
    private final int spinBalance;
    private final int coinsBalance;
    private final int missionPointGoal;
    private final List<Integer> digits;
    private final Map<String, Integer> rewards;

    public SpinResult(int pointBalance, int spinBalance, int coinsBalance, int missionPointGoal, List<Integer> digits, Map<String, Integer> rewards) {
        super(0);
        this.pointBalance = pointBalance;
        this.spinBalance = spinBalance;
        this.coinsBalance = coinsBalance;
        this.missionPointGoal = missionPointGoal;
        this.digits = digits;
        this.rewards = rewards;
    }

    public SpinResult(String errorMessage, int pointBalance, int spinBalance, int coinsBalance, int missionPointGoal) {
        super(1, errorMessage);
        this.pointBalance = pointBalance;
        this.spinBalance = spinBalance;
        this.coinsBalance = coinsBalance;
        this.missionPointGoal = missionPointGoal;
        this.digits = List.of();
        this.rewards = Map.of();
    }
}
