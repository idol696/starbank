package com.skypro.starbank.techsupport;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// инсталляция демо кода - служит основной и примером сервиса
@RestController
@RequestMapping("/test")
public class TechSupportController {
    private final TechSupportService techSupportService;

    public TechSupportController(TechSupportService techSupportService) {
        this.techSupportService = techSupportService;
    }
    @GetMapping
    public String initQuestion() {
        System.out.println(techSupportService.test());
        return "Работает! Смотри консоль!";
    }
}
