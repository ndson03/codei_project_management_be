package dpp.codei_project_management_be.dto.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoginResponse {

    private final String accessToken;
    private final String tokenType = "Bearer";
}

