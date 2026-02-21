package com.example.eldercare.controller;

import com.example.eldercare.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final ElderRepository elderRepository;
    private final BedRepository bedRepository;
    private final HealthRecordRepository healthRecordRepository;
    private final NursingRecordRepository nursingRecordRepository;
    private final AlarmRecordRepository alarmRecordRepository;
    private final FeeRecordRepository feeRecordRepository;
    private final DiningRecordRepository diningRecordRepository;
    private final ShiftScheduleRepository shiftScheduleRepository;
    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;

    @GetMapping("/stats")
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        stats.put("elderCount", elderRepository.count());
        stats.put("bedCount", bedRepository.count());
        stats.put("occupiedBedCount", bedRepository.countByStatus(1));
        stats.put("healthRecordCount", healthRecordRepository.count());
        stats.put("nursingRecordCount", nursingRecordRepository.count());
        stats.put("pendingAlarmCount", alarmRecordRepository.countByStatus(0));
        stats.put("handledAlarmCount", alarmRecordRepository.countByStatus(1));
        stats.put("feeRecordCount", feeRecordRepository.count());
        stats.put("diningRecordCount", diningRecordRepository.count());
        stats.put("shiftScheduleCount", shiftScheduleRepository.count());
        stats.put("activityCount", activityRepository.count());
        stats.put("userCount", userRepository.count());

        return stats;
    }
}
