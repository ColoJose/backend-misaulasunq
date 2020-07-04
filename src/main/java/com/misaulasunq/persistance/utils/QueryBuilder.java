package com.misaulasunq.persistance.utils;

import com.misaulasunq.model.*;
import com.misaulasunq.utils.SearchFilter;
import com.misaulasunq.controller.wrapper.SubjectFilterRequestWrapper;

import javax.persistence.criteria.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class QueryBuilder {

    private final SubjectFilterRequestWrapper filterRequestWrapper;
    private final CriteriaBuilder criteriaBuilder;
    private Boolean bySubjectName;
    private Boolean byHours;
    private Boolean byClassroomNumber;
    private Boolean byDay;

    public QueryBuilder(CriteriaBuilder criteriaBuilder, SubjectFilterRequestWrapper filterRequestWrapper) {
        this.filterRequestWrapper = filterRequestWrapper;
        this.criteriaBuilder = criteriaBuilder;
        this.bySubjectName = false;
        this.byHours = false;
        this.byClassroomNumber = false;
        this.byDay = false;
    }

    public CriteriaQuery<Subject> subjectByFilterRequestCriteria(){
        CriteriaQuery<Subject> criteriaQuery = criteriaBuilder.createQuery(Subject.class);
        Root<Subject> rootTable = criteriaQuery.from(Subject.class);
        List<Predicate> criteriaAndList = new ArrayList<>();
        Join<Subject, Commission> commissions = null; //Obs: te obliga a inicializarla
        Join<Commission, Schedule> schedules = null; //Obs: te obliga a inicializarla
        Join<Schedule, Classroom> classrooms = null; //Obs: te obliga a inicializarla

        this.processFilterFlags();

        if(byClassroomNumber || byHours || byDay){
            commissions = rootTable.join("commissions", JoinType.INNER); // El Inner es el Join simple (JOIN Aka INNER JOIN)
            schedules = commissions.join("schedules", JoinType.INNER);
        }

        if(byClassroomNumber){
            classrooms = schedules.join("classroom", JoinType.INNER);
        }


        if(bySubjectName){
            criteriaAndList.add(
                    criteriaBuilder.equal(
                            rootTable.get("name"),
                            filterRequestWrapper.getSubjectName())
            );
        }

        if(byHours){
            Predicate startLessThanOrEqualFilterStart =
                    criteriaBuilder.lessThanOrEqualTo(
                            schedules.get("startTime"),
                            filterRequestWrapper.getStartTime()
                    );
            Predicate endGreaterThanOrEqualFilterEnd =
                        criteriaBuilder.greaterThanOrEqualTo(
                                schedules.get("endTime"),
                                filterRequestWrapper.getEndTime()
                        );
            Predicate filterHoursInsideScheduleHours =
                criteriaBuilder.and(
                    startLessThanOrEqualFilterStart,
                    endGreaterThanOrEqualFilterEnd
                );

            Predicate startBetweenFilterHours =
                    criteriaBuilder.between(
                            schedules.get("startTime"),
                            filterRequestWrapper.getStartTime(),
                            filterRequestWrapper.getEndTime()
                    );
            Predicate endBetweenFilterHours =
                    criteriaBuilder.between(
                            schedules.get("endTime"),
                            filterRequestWrapper.getStartTime(),
                            filterRequestWrapper.getEndTime()
                    );
            criteriaAndList.add(
                criteriaBuilder.or(
                    startBetweenFilterHours,
                    endBetweenFilterHours,
                    filterHoursInsideScheduleHours
                )
            );
        }

        if(byDay){
            criteriaAndList.add(
                criteriaBuilder.equal(
                    schedules.get("day"),
                    filterRequestWrapper.getEnumDay()
                )
            );
        }

        if(byClassroomNumber){
            criteriaAndList.add(
                criteriaBuilder.equal(
                    classrooms.get("number"),
                    filterRequestWrapper.getClassroomNumber()
                )
            );
        }

        Predicate whereClause = criteriaBuilder.and(criteriaAndList.toArray(new Predicate[0]));
        Expression<Integer> groupByClause = rootTable.get("id");
        Order orderByClause = criteriaBuilder.desc(rootTable.get("name"));

        criteriaQuery.select(rootTable).where(whereClause).groupBy(groupByClause).orderBy(orderByClause);

        return criteriaQuery;
    }

    private void processFilterFlags(){
        for (SearchFilter eachFilter : filterRequestWrapper.getSearchFilters()){
            switch (eachFilter) {
                case BY_SUBJECT:
                    this.bySubjectName = true;
                    break;
                case BY_CLASSROOM:
                    this.byClassroomNumber = true;
                    break;
                case BY_SCHEDULE:
                    this.byHours = true;
                    break;
                case BY_DAY:
                    this.byDay = true;
                    break;
                default:
            }
        }
    }

}
