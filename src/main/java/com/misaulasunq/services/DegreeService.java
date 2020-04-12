package com.misaulasunq.services;

import com.misaulasunq.service.DegreeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DegreeService {

    @Autowired
    private DegreeRepository degreeRepository;
}
