package com.stark.webbanhang.api.admin.controller;
import com.nimbusds.jose.JOSEException;
import com.stark.webbanhang.api.user.dto.request.*;
import com.stark.webbanhang.api.user.dto.response.AuthenticationResponse;
import com.stark.webbanhang.api.user.dto.response.IntrospectResponse;
import com.stark.webbanhang.api.user.dto.response.UserResponse;
import com.stark.webbanhang.api.user.service.impl.AuthenticationService;
import com.stark.webbanhang.api.user.service.impl.UserService;
import com.stark.webbanhang.helper.base.constant.StatusMessage;
import com.stark.webbanhang.helper.base.response.ResponseObject;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;
    UserService userService;

    @PostMapping("/login")
    ResponseObject<AuthenticationResponse> logIn(@ModelAttribute AuthenticateRequest request){
        var result = authenticationService.authenticate(request);
        return ResponseObject.<AuthenticationResponse>builder()
                .message(StatusMessage.SUCCESS)
                .code(200)
                .data(result)
                .build();
    }
    @PostMapping("/signup")
    ResponseObject<UserResponse> signUp(@ModelAttribute UserRequest request){
        var result = userService.createRequest(request);
        return ResponseObject.<UserResponse>builder()
                .message(StatusMessage.SUCCESS)
                .code(200)
                .data(result)
                .build();
    }

    @PostMapping("/introspect")
    ResponseObject<IntrospectResponse> logIn(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);
        return ResponseObject.<IntrospectResponse>builder()
                .message(StatusMessage.SUCCESS)
                .code(200)
                .data(result)
                .build();
    }

    @PostMapping("/logout")
    ResponseObject<Void> logOut(@RequestBody LogoutRequest request)
            throws ParseException, JOSEException {
        authenticationService.logout(request);
        return ResponseObject.<Void>builder()
                .message(StatusMessage.SUCCESS)
                .code(200)
                .build();
    }

    @PostMapping("/refresh")
    ResponseObject<AuthenticationResponse> refresh(@RequestBody RefreshRequest request) throws ParseException, JOSEException {
        var result = authenticationService.refreshToken(request);
        return ResponseObject.<AuthenticationResponse>builder()
                .data(result)
                .message(StatusMessage.SUCCESS)
                .code(200)
                .build();
    }
}
