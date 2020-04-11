package repositories;


import model.Subject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class SubjectRepositoryTest {

    @MockBean
    private SubjectRepository subjectRepository;
    
    private Subject getSubject(){
        Subject subject = new Subject();
        subject.setId(1);
        subject.setName("pf");
        return subject;
    }

    @Test
    public void saveEntityInDb(){

        subjectRepository.save(getSubject());
        Subject savedSubject = subjectRepository.getOne(1);

    }
}
