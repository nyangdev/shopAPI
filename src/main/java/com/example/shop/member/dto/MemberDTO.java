package com.example.shop.member.dto;

import com.example.shop.member.entity.MemberEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MemberDTO {

    // MemberEntity 정보를 담는 DTO

    private String mid;
    private String mpw;
    private String mname;
    private String email;
    private LocalDateTime joinDate;
    private LocalDateTime modifiedDate;
    private String role;

    // JWT payload를 만들때 사용할 데이터를 Map 타입으로 반환하는 기능
    public Map<String, Object> getDataMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("mid", mid);
        map.put("mname", mname);
        map.put("email", email);
        map.put("role", role);

        return map;
    }

    // 생성자
    public MemberDTO(MemberEntity memberEntity) {
        this.mid = memberEntity.getMid();
        this.mpw = memberEntity.getMpw();
        this.mname = memberEntity.getMname();
        this.email = memberEntity.getEmail();
        this.joinDate = memberEntity.getJoinDate();
        this.modifiedDate = memberEntity.getModifiedDate();
        this.role = memberEntity.getRole();
    }
}
