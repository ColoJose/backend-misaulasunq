package com.misaulasunq.utils;

import com.misaulasunq.controller.dto.CommissionDTO;
import com.misaulasunq.exceptions.InvalidDayException;
import com.misaulasunq.exceptions.InvalidSemesterException;
import com.misaulasunq.model.Classroom;
import com.misaulasunq.model.Commission;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class CommissionUpdater {

    public CommissionUpdater() { }

    public void update(List<Commission> subjectCommission, List<CommissionDTO> commissionsDTO, Map<String, Classroom> classroomMap) throws InvalidDayException, InvalidSemesterException {
        List<CommissionMap> commissionMaps = new ArrayList<CommissionMap>();

        for( Commission com : subjectCommission ) {
            commissionMaps.add(this.createCommissionMap(com, commissionsDTO, classroomMap));
        }

        for ( CommissionMap commissionMap : commissionMaps) {
            commissionMap.update();
        }
    }

    private CommissionMap createCommissionMap(Commission commission, List<CommissionDTO> commissionsDTO, Map<String, Classroom> classroomMap) {
        Optional<CommissionDTO> commissionDTO = commissionsDTO.stream().filter(comDto -> comDto.getId().equals(commission.getId())).findFirst();
        return new CommissionMap(commission, commissionDTO.get(), classroomMap);
    }
}
