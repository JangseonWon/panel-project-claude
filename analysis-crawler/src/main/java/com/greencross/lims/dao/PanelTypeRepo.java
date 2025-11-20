package com.greencross.lims.dao;

import com.greencross.lims.entity.PanelType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PanelTypeRepo extends JpaRepository<PanelType, Long> {
    List<PanelType> getPanelTypeByPanelAndEffectiveDateLessThanEqualAndExpirationDateGreaterThan(String panel, LocalDateTime effectiveDate, LocalDateTime expirationDate);
}
