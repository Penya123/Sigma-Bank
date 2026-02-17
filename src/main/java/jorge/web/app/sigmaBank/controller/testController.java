package jorge.web.app.sigmaBank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/test")
    public String testConnection(){
        int result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);

        return "DB Connected: " + result;
    }
}
