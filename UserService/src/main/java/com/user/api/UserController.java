package com.user.api;

import com.user.dto.UserDto;
import com.user.exception.DuplicateResourceException;
import com.user.exception.ResourceNotFoundException;
import com.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto saveUserDetails(@RequestBody UserDto userDto) throws DuplicateResourceException {
        return userService.save(userDto);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public UserDto search(@RequestParam(required = false) String id,
                            @RequestParam(required = false) String email) throws UnsupportedOperationException, ResourceNotFoundException {
        if (id != null) {
            return userService.get(id);
        } else if (email != null) {
            return userService.findByEmail(email);
        } else {
            throw new UnsupportedOperationException("No Param is provided");
        }
    }
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public UserDto update(@RequestBody UserDto userDto) throws ResourceNotFoundException {
      return userService.update(userDto);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable String userId) throws ResourceNotFoundException {
        userService.delete(userId);
    }


    @GetMapping("/fetch-all")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> fetchAllUsers(){
      return userService.fetchAll();
    }

}
