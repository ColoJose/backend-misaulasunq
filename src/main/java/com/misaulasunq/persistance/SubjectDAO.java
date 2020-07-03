package com.misaulasunq.persistance;

import com.misaulasunq.model.Subject;
import com.misaulasunq.persistance.utils.QueryBuilder;
import com.misaulasunq.controller.wrapper.SubjectFilterRequestWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class SubjectDAO {

    @Autowired
    private EntityManager entityManager;

    public List<Subject> getSubjectByFilterRequest(SubjectFilterRequestWrapper filterRequestWrapper){
        QueryBuilder queryBuilder =
            new QueryBuilder(
                this.entityManager.getCriteriaBuilder(),
                filterRequestWrapper
            );


        TypedQuery<Subject> query =
            this.entityManager.createQuery(
                    queryBuilder.subjectByFilterRequestCriteria()
            );

        return query.getResultList();
    }
}
