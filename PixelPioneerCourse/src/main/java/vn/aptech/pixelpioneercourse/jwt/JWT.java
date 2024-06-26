package vn.aptech.pixelpioneercourse.jwt;

import vn.aptech.pixelpioneercourse.dto.Authorized;

import java.time.LocalDateTime;
import java.util.List;

public interface JWT {
    String encode(int id, List<String> roles, LocalDateTime expiredAt, String secret);

    Authorized decode(String token, String secret);


}
