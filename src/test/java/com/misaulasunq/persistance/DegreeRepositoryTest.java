package com.misaulasunq.persistance;

import com.misaulasunq.model.Degree;
import com.misaulasunq.utils.DegreeBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@Rollback
@Transactional
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DegreeRepositoryTest {

    @Autowired
    private DegreeRepository degreeRepository;

    @Test
    public void ifFindByCodeOfTwoDegrees_OnlyTheseAreRetrieved() {
        //Setup(Given)
        Degree aDegree = DegreeBuilder.buildADegree().withMockData().withDegreeCode("102").build();
        Degree aDegree1 = DegreeBuilder.buildADegree().withMockData().withDegreeCode("103").build();
        Degree aDegree2 = DegreeBuilder.buildADegree().withMockData().withDegreeCode("104").build();
        Degree aDegree3 = DegreeBuilder.buildADegree().withMockData().withDegreeCode("105").build();
        Degree aDegree4 = DegreeBuilder.buildADegree().withMockData().withDegreeCode("106").build();
        degreeRepository.saveAll(List.of(aDegree,aDegree1,aDegree2,aDegree3,aDegree4));

        //Exercise(When)
        List<Degree> degreesRetrieved = degreeRepository.findAllByCodeInOrderByCodeAsc(Set.of("102","105"));

        //Test(Then)
        assertEquals(2, degreesRetrieved.size());
        assertEquals("102", degreesRetrieved.get(0).getCode());
        assertEquals("105", degreesRetrieved.get(1).getCode());
    }
}