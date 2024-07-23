package com.hodbenor.project.slot.machine.data;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hodbenor.project.slot.machine.data.beans.User;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Map;

@Repository
public class UserRepository {
    private static final String USER_PREFIX = "user:";
    private static final String USER_POINTS_PREFIX = "user:points:";
    private static final String USER_SPINS_PREFIX = "user:spins:";
    private static final String USER_COINS_PREFIX = "user:coins:";
    private static final String USER_MISSION_PREFIX = "user:mission:";
    private static final String LOCK_KEY_SPINS = "resource_lock_spins";
    private final JedisPool jedisPool;

    public UserRepository() {
        this.jedisPool = new JedisPool(new JedisPoolConfig(), "localhost", 6379);
    }

    public void saveUser(User user) {
        try (Jedis jedis = jedisPool.getResource()) {
            ObjectMapper objectMapper = new ObjectMapper();
            String userJson = objectMapper.writeValueAsString(user);
            jedis.set(USER_PREFIX + user.id(), userJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public User getUser(long userId) {
        try (Jedis jedis = jedisPool.getResource()) {
            String userJson = jedis.get(USER_PREFIX + userId);
            if (userJson != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readValue(userJson, User.class);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public Map<String, String> getCurrentUserMission(long userId) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hgetAll(USER_MISSION_PREFIX + userId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int getPointsBalance(long userId) {
        return getIntItem(userId, USER_POINTS_PREFIX);
    }

    public int getSpinsBalance(long userId) {
        return getIntItem(userId, USER_SPINS_PREFIX);
    }

    public int getCoinsBalance(long userId) {
        return getIntItem(userId, USER_COINS_PREFIX);
    }

    public void decrUserSpinsBalance(long userId, long decrBy) {
        RedisLock lock = null;
        try (Jedis jedis = jedisPool.getResource()) {
            lock = new RedisLock(jedis, LOCK_KEY_SPINS);
            if (lock.acquireLock()) {
                jedis.decrBy(USER_SPINS_PREFIX + userId, decrBy);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (lock != null) {
                lock.releaseLock();
            }
        }
    }

    private int getIntItem(long userId, String keyPrefix) {
        try (Jedis jedis = jedisPool.getResource()) {
            String numStr = jedis.get(keyPrefix + userId);
            return numStr != null ? Integer.parseInt(numStr) : 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
