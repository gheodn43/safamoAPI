package com.backend.restapi.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class RequestRateLimiterService {
    private final Map<Integer, Long> userLastRequestTimes = new ConcurrentHashMap<>();

    public void checkRateLimit(int userId, long timeLimitInHours) {
        if (userLastRequestTimes.containsKey(userId)) {
            long lastRequestTime = userLastRequestTimes.get(userId);
            long currentTime = System.currentTimeMillis();
            long timeDifference = currentTime - lastRequestTime;
            long hoursDifference = TimeUnit.MILLISECONDS.toHours(timeDifference);

            if (hoursDifference < timeLimitInHours) {
                throw new RuntimeException("Bạn chỉ có thể tạo yêu cầu mỗi " + timeLimitInHours + " giờ.");
            }
        }

        userLastRequestTimes.put(userId, System.currentTimeMillis());
    }
}

