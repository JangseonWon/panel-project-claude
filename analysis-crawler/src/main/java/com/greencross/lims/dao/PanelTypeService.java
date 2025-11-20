package com.greencross.lims.dao;

import com.greencross.lims.entity.PanelType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class PanelTypeService {
    private final PanelTypeRepo repo;
    @Transactional
    public List<PanelType> getPanelType(String panel) {
        LocalDateTime now = LocalDateTime.now();
        List<PanelType> panels = repo.getPanelTypeByPanelAndEffectiveDateLessThanEqualAndExpirationDateGreaterThan(panel.toUpperCase(), now, now);
        if(panels.isEmpty()) throw new InvalidParameterException("Panel invalid : " + panel);
        return panels;
    }
}
