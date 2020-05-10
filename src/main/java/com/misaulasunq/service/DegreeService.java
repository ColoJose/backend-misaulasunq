package com.misaulasunq.service;

import com.misaulasunq.exceptions.DegreeNotFoundException;
import com.misaulasunq.model.Degree;
import com.misaulasunq.persistance.DegreeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DegreeService {

    @Autowired
    private DegreeRepository degreeRepository;

    public void save(Degree degree) { this.degreeRepository.save(degree); }

    public Degree findDegreeById(Integer id) throws DegreeNotFoundException {
        return this.degreeRepository.findById(id)
                                    .orElseThrow( () -> new DegreeNotFoundException(id));
    }

    public List<Degree> findAll() { return this.degreeRepository.findAll(); }
}
