package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserCreateData;
import com.codesoom.assignment.dto.UserResultData;
import com.codesoom.assignment.dto.UserUpdateData;
import com.codesoom.assignment.errors.UserEmailDuplicatedException;
import com.codesoom.assignment.errors.UserNotFoundException;
import com.github.dozermapper.core.Mapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 사용자에 대한 요청을 수행한다.
 */
@Service
@Transactional
public class UserService {
    private final Mapper mapper;
    private final UserRepository userRepository;

    public UserService(Mapper dozerMapper, UserRepository userRepository) {
        this.mapper = dozerMapper;
        this.userRepository = userRepository;
    }

    public UserResultData getUserResultData(User user) {
        return UserResultData.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }

    /**
     * 주어진 식별자에 해당하는 사용자를 리턴한다.
     *
     * @param id - 조회하려는 사용자의 식별자
     * @return 주어진 {@code id}에 해당하는 사용자
     * @throws UserNotFoundException 만약
     *         {@code id}에 해당되는 사용자가 저장되어 있지 않은 경우
     */
    public UserResultData getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return getUserResultData(user);
    }


    public List<UserResultData> getUsers() {
        List<User> users =  userRepository.findAll();
        return users.stream()
                .map(this::getUserResultData)
                .collect(Collectors.toList());
    }

    /**
     * 주어진 사용자 저장하고 해당 사용자를 리턴한다.
     *
     * @param userCreateData - 새로 저장하고자 하는 사용자
     * @return 저장 된 사용자
     */
    public UserResultData createUser(UserCreateData userCreateData) {
        String email = userCreateData.getEmail();
        if(userRepository.existsByEmail(email)) {
            throw new UserEmailDuplicatedException(email);
        }

        User user = mapper.map(userCreateData, User.class);
        User savedUser = userRepository.save(user);

       return getUserResultData(savedUser);
    }

    /**
     * 주어진 식별자에 해당하는 서용자를 수정하고 해당 사용자를 리턴한다.
     *
     * @param id - 수정하고자 하는 사용자의 식별자
     * @param userUpdateData - 수정 할 새로운 사용자
     * @return 수정 된 사용자
     * @throws UserNotFoundException 만약
     *         {@code id}에 해당되는 사용자가 저장되어 있지 않은 경우
     */
    public UserResultData updateUser(Long id, UserUpdateData userUpdateData) {
        UserResultData userResultData = getUser(id);

        mapper.map(userUpdateData, userResultData);

        return userResultData;
    }

    /**
     * 주어진 식별자에 해당하는 사용자를 삭제하고 해당 사용자를 리턴한다.
     *
     * @param id - 삭제하고자 하는 사용자의 식별자
     * @return 삭제 된 사용자
     * @throws UserNotFoundException 만약 주어진
     *         {@code id}에 해당되는 사용자가 저장되어 있지 않은 경우
     */
    public UserResultData deleteUser(Long id) {
        UserResultData userResultData = getUser(id);

        User deleteUser = User.builder()
                .id(userResultData.getId())
                .name(userResultData.getName())
                .email(userResultData.getEmail())
                .password(userResultData.getPassword())
                .build();

        userRepository.delete(deleteUser);

        return userResultData;
    }
}
