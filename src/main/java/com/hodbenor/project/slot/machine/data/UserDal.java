package com.hodbenor.project.slot.machine.data;

import com.hodbenor.project.slot.machine.data.beans.User;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UserDal {
    public static final String MISSION_POINTS_GOAL_KEY = "missionPointsGoal";
    private final UserRepository userRepository;

    public UserDal(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser(long userId) {
        return userRepository.getUser(userId);
    }

    public int getCurrentMissionPointsGoal(User user) {
        Map<String, String> userMissionData =  userRepository.getCurrentUserMission(user.id());
        String missionPointGoal = userMissionData.get(MISSION_POINTS_GOAL_KEY);

        return missionPointGoal != null ? Integer.parseInt(missionPointGoal) : 0;
    }

    public void decrSpinsBalance(User user, int decrBy) {
        userRepository.decrUserSpinsBalance(user.id(), decrBy);
    }

    public int getPointsBalance(User user) {
        return userRepository.getPointsBalance(user.id());
    }

    public int getSpinsBalance(User user) {
        return userRepository.getSpinsBalance(user.id());
    }

    public int getCoinsBalance(User user) {
        return userRepository.getCoinsBalance(user.id());
    }
}