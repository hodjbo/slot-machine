package com.hodbenor.project.slot.machine.service;

import com.hodbenor.project.slot.machine.data.UserDal;
import com.hodbenor.project.slot.machine.data.beans.User;
import com.hodbenor.project.slot.machine.rest.client.AccumulationClient;
import com.hodbenor.project.slot.machine.rest.client.WinSlotMachineResponse;
import com.hodbenor.project.slot.machine.service.beans.SpinResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SlotMachineServiceImpl implements SlotMachineService {

    public static final String INSUFFICIENT_SPINS = "insufficient spins balance";
    private final UserDal userDal;
    private final AccumulationClient accumulationClient;

    public SlotMachineServiceImpl(UserDal userDal, AccumulationClient accumulationClient) {
        this.userDal = userDal;
        this.accumulationClient = accumulationClient;
    }

    @Override
    public SpinResult spin(User user) {

        boolean userAllowToSpin = isUserAllowToSpin(user);
        if (!userAllowToSpin) {
            return new SpinResult(INSUFFICIENT_SPINS, userDal.getPointsBalance(user), userDal.getSpinsBalance(user),
                    userDal.getCoinsBalance(user), userDal.getCurrentMissionPointsGoal(user));
        }

        userDal.decrSpinsBalance(user, 1);
        List<Integer> digits = List.of(randomSpin(), randomSpin(), randomSpin());
        Map<String, Integer> rewards = Map.of();
        if (digits.stream().allMatch(digits.get(0)::equals)) {
            WinSlotMachineResponse winSlotMachineResponse = accumulationClient.winSlotMachine(user, digits);
            rewards = winSlotMachineResponse.rewards();
        }

        return new SpinResult(userDal.getPointsBalance(user), userDal.getSpinsBalance(user), userDal.getCoinsBalance(user),
                userDal.getCurrentMissionPointsGoal(user), digits, rewards);
    }

    private boolean isUserAllowToSpin(User user) {

        return userDal.getSpinsBalance(user) > 0;
    }

    private int randomSpin() {

        return 57;///(int) ((Math.random() * 10));
    }
}
