package ru.nsu.fit.crocodile;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/info")
public class InfoController {
    @GetMapping("/ping")
    public HttpStatus ping() {
        return HttpStatus.OK;
    }
}
