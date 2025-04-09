package com.lehaitien.gym.application.service;

import com.lehaitien.gym.domain.exception.AppException;
import com.lehaitien.gym.domain.exception.ErrorCode;
import com.lehaitien.gym.domain.model.User.User;
import com.lehaitien.gym.domain.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Log4j2
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class OtpService {

    UserRepository userRepository;
     StringRedisTemplate redisTemplate;
    EmailService emailService;
    PasswordEncoder passwordEncoder;
    private static final Duration TTL = Duration.ofMinutes(5);

    public void sendOtp(String userName,String email) {
        // ✅ Kiểm tra email có tồn tại trong hệ thống
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if(!userRepository.existsByEmail(email)){
            throw new AppException(ErrorCode.EMAIL_NOT_EXISTED);
        }

        // ✅ Sinh mã OTP ngẫu nhiên
        String otp = String.format("%06d", ThreadLocalRandom.current().nextInt(0, 999999));

        // ✅ Lưu OTP vào Redis với TTL
        saveOtp(userName,otp);

        // ✅ Gửi email
        String subject = "🛡️ Mã OTP đặt lại mật khẩu (Gym CRM)";
        String body = String.format("""
                Xin chào %s,

                Mã OTP của bạn để đặt lại mật khẩu là: %s
                Mã này sẽ hết hạn sau %d phút.

                Nếu bạn không yêu cầu, vui lòng bỏ qua email này.

                Trân trọng,
                Gym CRM Team
                """, user.getFullName(), otp, TTL.toMinutes());

        emailService.sendEmail(email, subject, body);

        log.info("✅ Gửi OTP [{}] đến email: {}", otp, email);
    }

    public void resetPasswordByOtp(String username,String email, String otp, String newPassword) {
        String redisKey = "otp:" + username;
        String savedOtp = redisTemplate.opsForValue().get(redisKey);

        if (savedOtp == null || !savedOtp.equals(otp)) {
            throw new AppException(ErrorCode.INVALID_OTP);
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        redisTemplate.delete(redisKey); // ✅ Xóa OTP sau khi dùng

        log.info(" Mật khẩu đã được đặt lại cho {}", email);
    }

    public void saveOtp(String username, String otp) {
        String key = "otp:" + username;
        redisTemplate.opsForValue().set(key, otp, TTL);
    }

    public String getOtp(String username) {
        return redisTemplate.opsForValue().get("otp:" + username);
    }

    public void deleteOtp(String email) {
        redisTemplate.delete("otp:" + email);
    }



}
