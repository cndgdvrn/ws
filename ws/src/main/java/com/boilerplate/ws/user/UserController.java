package com.boilerplate.ws.user;

import com.boilerplate.ws.auth.token.TokenService;
import com.boilerplate.ws.shared.GenericMessage;
import com.boilerplate.ws.shared.OverriddenMessage;
import com.boilerplate.ws.user.dto.UserCreate;
import com.boilerplate.ws.user.dto.UserDTO;
import com.boilerplate.ws.user.dto.UserUpdate;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Base64;


@RestController()
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private OverriddenMessage overriddenMessage;

    @Autowired
    private TokenService tokenService;


    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody @Valid UserCreate user) {
        String messageTemplate = overriddenMessage.getMessageFromLocale("boilerplate.user.created.successfully");
        userService.save(user.toUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(new GenericMessage(messageTemplate));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        User user = userService.getUser(id);
        UserDTO userDTO = new UserDTO(user);
        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }

    @GetMapping
    public ResponseEntity<?> getUsers(
            @PageableDefault(size = 5) Pageable pageable,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        User currentUser = tokenService.verifyToken(authorizationHeader);
        Page<UserDTO> users = userService.getUsers(pageable, currentUser).map(UserDTO::new);
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @RequestBody(required = false) @Valid UserUpdate userUpdate,
            @RequestHeader(value="Authorization",required = false) String authorizationHeader) {
        User currentUser = tokenService.verifyToken(authorizationHeader);
        User updatedUser = userService.updateUser(id, userUpdate, currentUser);
        UserDTO userDTO = new UserDTO(updatedUser);
        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }

    @PatchMapping("/activate")
    public ResponseEntity<?> activateUser(@RequestParam(required = false) String token) {
        userService.activateUser(token);
        String messageTemplate = overriddenMessage.getMessageFromLocale("boilerplate.activation.user.successfully");
        return ResponseEntity.status(HttpStatus.OK).body(new GenericMessage(messageTemplate));
    }

}







