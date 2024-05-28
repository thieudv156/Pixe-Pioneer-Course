package vn.aptech.pixelpioneercourse.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.aptech.pixelpioneercourse.entities.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    @Modifying
    @Transactional
    @Query("UPDATE RefreshToken o SET o.available=false WHERE o.user.id=:userId AND o.available=true")
    void disableRefreshTokenFromUser(@Param("userId") int userId);
    
    //can du lieu gi thi` load du lieu do len, ko join thi se load toan bo bang lien quan (co rang buoc khoa ngoai)
    @Query("SELECT o FROM RefreshToken o JOIN FETCH o.user u JOIN FETCH u.role WHERE o.code=:code AND o.available=true")
    Optional<RefreshToken> findRefreshTokenByCode(@Param("code") String code);
}
