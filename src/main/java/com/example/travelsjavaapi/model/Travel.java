package com.example.travelsjavaapi.model;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.example.travelsjavaapi.enumeration.TravelTypeEnum;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Travel {

    private Long id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private BigDecimal amount;
    private TravelTypeEnum type;

    public Travel(TravelTypeEnum type) {
        this.type = type;
    }
}
