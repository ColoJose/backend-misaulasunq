package com.misaulasunq.utils;

import com.misaulasunq.controller.dto.CommissionDTO;
import com.misaulasunq.controller.dto.ScheduleDTO;
import com.misaulasunq.exceptions.InvalidDayException;
import com.misaulasunq.exceptions.InvalidSemesterException;
import com.misaulasunq.model.Classroom;
import com.misaulasunq.model.Commission;
import com.misaulasunq.model.Schedule;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CommissionMap {
    private Commission commission;
    private CommissionDTO commissionDTO;
    private Map<String, Classroom> classroomMap;

    public CommissionMap(Commission commission, CommissionDTO commissionDTO, Map<String, Classroom> classroomMap) {
        this.commission = commission;
        this.commissionDTO = commissionDTO;
        this.classroomMap = classroomMap;
    }

    public Commission update() throws InvalidSemesterException, InvalidDayException {
        commission.setName(commissionDTO.getName());
        commission.setYear(commissionDTO.getYear());
        commission.setSemester(SemesterConverter.stringToSemester(commissionDTO.getSemester()));
        this.updateSchedules(commission.getSchedules(),commissionDTO.getSchedules());

        return commission;
    }

    private void updateSchedules(List<Schedule> schedules, List<ScheduleDTO> schedulesDTO) throws InvalidDayException {
        List<ScheduleMap> scheduleMaps = new ArrayList<ScheduleMap>();

        for(Schedule schedule : schedules) {
            scheduleMaps.add(this.createScheduleMaps(schedule, schedulesDTO));
        }

        for( ScheduleMap scheduleMap : scheduleMaps) {
            scheduleMap.update();
        }
    }

    private ScheduleMap createScheduleMaps(Schedule schedule, List<ScheduleDTO> schedulesDTO) {
        Optional<ScheduleDTO> scheduleDTOAux = schedulesDTO.stream().filter(scheduleDTO -> scheduleDTO.getId().equals(schedule.getId())).findFirst();
        return new ScheduleMap(schedule, scheduleDTOAux.get(), classroomMap);
    }
}
