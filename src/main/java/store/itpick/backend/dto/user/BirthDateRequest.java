package store.itpick.backend.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor
public class BirthDateRequest {
    @NotBlank(message = "birth_date: 필수입니다.")
    @Length(min = 6, max = 6, message = "birth_date: 정확히 {max}자리여야 합니다")
    private String birth_date;
}
