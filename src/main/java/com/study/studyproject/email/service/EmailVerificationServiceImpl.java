package com.study.studyproject.email.service;

import com.study.studyproject.global.exception.ex.BadRequestException;
import com.study.studyproject.global.hash.HashUtil;
import com.study.studyproject.email.repository.EmailVerificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;

import static com.study.studyproject.global.exception.ex.ErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailVerificationServiceImpl implements EmailVerificationService {

    private static final Duration CODE_TTL = Duration.ofMinutes(5);
    private static final Duration VERIFIED_TTL = Duration.ofMinutes(30);
    private static final Duration COOLDOWN_TTL = Duration.ofSeconds(60);
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final EmailVerificationRepository emailVerificationRepository;
    private final EmailSender emailSender;

    @Override
    public void sendCode(String email) {
        String hash = HashUtil.sha256(email);

        if (!emailVerificationRepository.tryStartCooldown(hash, COOLDOWN_TTL)) {
            throw new BadRequestException(EMAIL_CODE_COOLDOWN);
        }

        String code = generateCode();
        emailVerificationRepository.saveCode(hash, code, CODE_TTL);
        emailSender.send(email, code);
    }

    @Override
    public void verifyCode(String email, String code) {
        String hash = HashUtil.sha256(email);

        String stored = emailVerificationRepository.findCode(hash);
        if (stored == null) {
            throw new BadRequestException(EMAIL_CODE_NOT_FOUND);
        }
        if (!stored.equals(code)) {
            throw new BadRequestException(EMAIL_CODE_MISMATCH);
        }

        emailVerificationRepository.deleteCode(hash);
        emailVerificationRepository.markVerified(hash, VERIFIED_TTL);
    }

    @Override
    public boolean isVerified(String email) {
        String hash = HashUtil.sha256(email);
        return emailVerificationRepository.isVerified(hash);
    }

    @Override
    public void clearVerification(String email) {
        String hash = HashUtil.sha256(email);
        emailVerificationRepository.deleteVerified(hash);
    }

    private String generateCode() {
        int code = SECURE_RANDOM.nextInt(1_000_000);
        return String.format("%06d", code);
    }
}
