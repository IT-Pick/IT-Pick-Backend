package store.itpick.backend.service;

import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import store.itpick.backend.dto.user.user.PostUserRequest;
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

    public PostUserRequest createUser(PostUserRequest postUserRequest){
        String password = postUserRequest.getPassword();
        String encodedPassword = passwordEncoder.encode(password);
        postUserRequest.setPassword(encodedPassword);

        User user = User.builder()
                .email(postUserRequest.getEmail())
                .password(postUserRequest.getPassword())
                .nickname(postUserRequest.getNickname())
                .birth_date(postUserRequest.getBirth_date())
                .build();

        user = userRepository.save(user);

        return PostUserRequest.builder()
                .nickname(user.getNickname())
                .build();
    }
}
