package vn.aptech.pixelpioneercourse.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Authentication {
    private final UserInformation userInformation;
    private final String accessToken;
    private final String refreshToken;
    public final LocalDateTime expiredAt;
}
