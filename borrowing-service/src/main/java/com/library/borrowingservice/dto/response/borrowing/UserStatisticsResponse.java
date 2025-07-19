package com.library.borrowingservice.dto.response.borrowing;

import com.library.commonservice.dto.response.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStatisticsResponse {
    private long activeUsers;
    private long totalUsers;
    private long newRegistrations;
    private double avgBorrowsPerUser;
    private List<userDTO> topActiveUsers;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class userDTO {
        private Long userId;
        private String email;
        private String fullName;
        private long borrowCount;
    }
}
