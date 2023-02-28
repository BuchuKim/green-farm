package com.buchu.greenfarm.config.auth.dto;

import com.buchu.greenfarm.entity.Role;
import com.buchu.greenfarm.entity.User;
import com.buchu.greenfarm.exception.GreenFarmErrorCode;
import com.buchu.greenfarm.exception.GreenFarmException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Getter
@Setter
@ToString
@Builder
@Slf4j
public class OAuthAttributes {
    private String nameAttributeKey;
    private String id;
    private String name;
    private String email;
    private String picture;
    private String registrationId;

    public  static OAuthAttributes of(String registrationId,
                                      String userNameAttributeName,
                                      Map<String, Object> attributes) {
        if (registrationId.equalsIgnoreCase("naver")) {
            return ofNaver(userNameAttributeName, attributes, registrationId);
        } else if (registrationId.equalsIgnoreCase("google")) {
            return ofGoogle(userNameAttributeName, attributes, registrationId);
        } else {
            throw new GreenFarmException(GreenFarmErrorCode.INVALID_OAUTH);
        }
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName,
                                            Map<String, Object> attributes,
                                            String registrationId) {
        // attributes : google 에서 제공하는 user 관련 info
        return OAuthAttributes.builder()
                .id((String) attributes.get(userNameAttributeName))
                .registrationId(registrationId)
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .nameAttributeKey("id")
                .build();
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName,
                                           Map<String, Object> attributes,
                                           String registrationId) {
        Map<String, Object> response = (Map<String, Object>) attributes.get(userNameAttributeName);
        response.put("registrationId",registrationId);
        return OAuthAttributes.builder()
                .id((String) response.get("id"))
                .registrationId(registrationId)
                .name((String) response.get("nickname"))
                .email((String) response.get("email"))
                .nameAttributeKey("id")
                .build();
    }

    public Map<String,Object> mapAttributes() {
        return new ObjectMapper().convertValue(this, Map.class);
    }

    public User toEntity() {
        return User.builder()
                .name(this.name)
                .email(this.email)
                .userId("_")
                .oAuth2Id(this.id)
                .registrationId(this.registrationId)
                .role(Role.GUEST)
                .build();
    }
}
