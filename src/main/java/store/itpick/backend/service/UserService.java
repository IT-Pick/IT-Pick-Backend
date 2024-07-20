package store.itpick.backend.service;

import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import store.itpick.backend.dto.UserDTO;
import store.itpick.backend.model.User;
import store.itpick.backend.repository.UserRepository;

@Service
@NoArgsConstructor
public class UserService {
    private BCryptPasswordEncoder passwordEncoder;
    private  UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder(); // passwordEncoder 초기화
    }

    public UserDTO createUser(UserDTO userDTO){
        String password = userDTO.getPassword();
        String encodedPassword = passwordEncoder.encode(password);
        userDTO.setPassword(encodedPassword);

        User user = User.builder()
                .id(userDTO.getId())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .nickname(userDTO.getNickname())
                .birth_date(userDTO.getBirth_date())
                .alect_setting(userDTO.getAlect_setting())
                .reference_code(userDTO.getReference_code())
                .image_url(userDTO.getImage_url())
                .refresh_token(userDTO.getRefresh_token())
                .status(userDTO.getStatus())
                .create_at(userDTO.getCreate_at())
                .update_at(userDTO.getUpdate_at())
                .build();

        user = userRepository.save(user);

        return UserDTO.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .build();
    }
}
