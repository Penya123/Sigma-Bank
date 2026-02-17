package jorge.web.app.sigmaBank.serive;

import jorge.web.app.sigmaBank.dto.UserDto;
import jorge.web.app.sigmaBank.entity.User;
import jorge.web.app.sigmaBank.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(UserDto userDto){
        User user = new User(
                userDto.getFirstName(),
                userDto.getLastName(),
                passwordEncoder.encode(userDto.getPassword()),
                userDto.getUsername(),
                "io_" + userDto.getUsername(),
                userDto.getDob(),
                List.of("USER")
        );
        return userRepository.save(user);
    }
}
