package it.uniroma3.siw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FooterController {

    @GetMapping("/privacy")
    public String privacyPage() {
        return "privacy";
    }

    @GetMapping("/terms")
    public String termsPage() {
        return "terms";
    }
    
    @GetMapping("/cookie")
    public String cookiePolicyPage() {
        return "cookie";
    }
    
    @GetMapping("/contact")
    public String contactPage() {
        return "contacts";
    }
}
