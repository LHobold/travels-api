package com.example.travelsjavaapi.service;

import java.util.List;
import java.math.BigDecimal;
import java.math.RoundingMode;

import com.example.travelsjavaapi.model.Statistic;
import com.example.travelsjavaapi.model.Travel;

import org.springframework.stereotype.Service;

@Service
public class StatisticsService {

        Statistic stats = new Statistic();

        public Statistic create(List<Travel> travels) throws Exception {
                try {
                        stats.setCount(travels.stream().count());
                        stats.setAvg(BigDecimal.valueOf(travels.stream().mapToDouble(t -> t.getAmount().doubleValue())
                                        .average().orElse(0.0)).setScale(2, RoundingMode.HALF_UP));
                        stats.setMax(BigDecimal.valueOf(travels.stream().mapToDouble(t -> t.getAmount().doubleValue())
                                        .max().orElse(0.0)).setScale(2, RoundingMode.HALF_UP));
                        stats.setMin(BigDecimal.valueOf(travels.stream().mapToDouble(t -> t.getAmount().doubleValue())
                                        .min().orElse(0.0)).setScale(2, RoundingMode.HALF_UP));
                        stats.setSum(BigDecimal
                                        .valueOf(travels.stream().mapToDouble(t -> t.getAmount().doubleValue()).sum())
                                        .setScale(2, RoundingMode.HALF_UP));

                        return stats;

                } catch (Exception e) {
                        System.out.println("SQLException: " + e.getMessage());
                        throw e;

                }
        }
}
