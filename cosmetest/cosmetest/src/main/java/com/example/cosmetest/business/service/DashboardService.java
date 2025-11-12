// src/main/java/com/example/cosmetest/business/service/DashboardService.java
package com.example.cosmetest.business.service;

import com.example.cosmetest.business.dto.*;
import java.util.List;

public interface DashboardService {
    DashboardStatsDTO getDashboardStats();
    DailyStatsDTO getDailyStats();
    List<RdvDTO> getProchainRdvs(int limit);
    List<EtudeDTO> getRecentEtudes(int limit);
    List<ActiviteRecenteDTO> getRecentActivities(int limit);
    List<RdvDTO> getUpcomingRdvs(int limit);
}