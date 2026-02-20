package jorge.web.app.sigmaBank.controller;

import jorge.web.app.sigmaBank.dto.UserDto;
import jorge.web.app.sigmaBank.entity.User;
import jorge.web.app.sigmaBank.serive.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public ResponseEntity<User> register(@RequestBody UserDto userDto){
        return ResponseEntity.ok(userService.registerUser(userDto));
    }

    public ResponseEntity<?> authenticateUser(@RequestBody UserDto userDto){
        var authObject = userService.authenticateUSer(userDto);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, (String) authObject.get("token"))
                .body(authObject.get("user"));
    }
}
