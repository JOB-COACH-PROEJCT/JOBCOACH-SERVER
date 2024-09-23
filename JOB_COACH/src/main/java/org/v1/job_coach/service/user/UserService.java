package org.v1.job_coach.service.user;

import org.v1.job_coach.dto.user.request.UserUpdateRequest;
import org.v1.job_coach.dto.user.response.UserDeleteResponse;
import org.v1.job_coach.dto.user.response.UserInfoResponse;
import org.v1.job_coach.dto.user.response.UserUpdateResponse;
import org.v1.job_coach.entity.User;

public interface UserService {

    UserInfoResponse getUserInfo(User user);

    UserUpdateResponse updateUserInfo(User user, UserUpdateRequest updateRequest);

    UserDeleteResponse deleteUser(User user);

}
