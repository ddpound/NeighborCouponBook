package com.neighborcouponbook.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PasswordChangeRequest {
    private Long   userId;
    private String oldPassword;
    private String newPassword;

    public boolean validate(PasswordChangeRequest passwordChangeRequest) {
        return passwordChangeRequest != null &&
                passwordChangeRequest.getUserId() != null &&
                passwordChangeRequest.getOldPassword() != null &&
                passwordChangeRequest.getNewPassword() != null &&
                !passwordChangeRequest.getNewPassword().isEmpty();
    }
}
