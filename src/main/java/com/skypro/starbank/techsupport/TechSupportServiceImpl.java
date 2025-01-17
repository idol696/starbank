package com.skypro.starbank.techsupport;

import com.skypro.starbank.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TechSupportServiceImpl implements TechSupportService {
    @Autowired
    Repository testRepository;

    @Override
    public int test() {
        return testRepository.getRandomTransactionAmount();
    }
}
