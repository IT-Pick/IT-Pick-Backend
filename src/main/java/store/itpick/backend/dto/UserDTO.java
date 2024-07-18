package store.itpick.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String email;
    private String password;
    private String nickname;
    private Long birth_date;
    private Boolean alect_setting;
    private String reference_code;
    private String image_url;
    private String refresh_token;
    private String status;
    private Timestamp create_at;
    private Timestamp update_at;

}
