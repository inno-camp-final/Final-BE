package com.havit.finalbe.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Member extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;
    

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column
    private Long imageId;

    @Column
    private String introduce;

    public boolean isValidateMember(Long memberId) {
        return this.memberId == memberId;
    }

    public boolean validatePassword(PasswordEncoder passwordEncoder, String password) {
        return passwordEncoder.matches(password, this.password);
    }

    public void update(Long imageId, String nickname, String introduce) {
        this.imageId = imageId;
        this.nickname = nickname;
        this.introduce = introduce;
    }

    public void edit(String password) {
        this.password = password;
    }

    public void deleteImg(Long imageId) {
        this.imageId = imageId;
    }

}
