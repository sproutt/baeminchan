package codesquad.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
public class UserRequestDTO {

    @NotBlank(message = "메일을 작성해주세요.")
    @Email(message = "메일의 양식을 지켜주세요.")
    private String email;

    @NotBlank(message = "이름을 작성해주세요.")
    @Size(min = 1, max = 20)
    private String name;

    @NotBlank(message = "전화번호를 작성해주세요.")
    @Pattern(regexp = "[0-9]{10,11}", message = "10~11자리의 숫자만 입력가능합니다")
    private String phoneNumber;

    @NotBlank(message = "password를 입력해주세요")
    @Pattern(regexp = "^[0-9a-zA-Z]+")
    private String password;

    @NotBlank(message = "password를 입력해주세요")
    @Pattern(regexp = "^[0-9a-zA-Z]+")
    private String password2;

    public User toUserEntity(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.getPassword());
        return new User(this);
    }
}