package com.example.urlshortener.Service;

import com.example.urlshortener.Entity.UrlMapping;
import com.example.urlshortener.Repository.UrlMappingRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.concurrent.ThreadLocalRandom;
@Service
public class UrlShortenerService {
    private static final int INITIAL_CODE_LENGTH = 6;
    private static final int MAX_CODE_LENGTH = 8;
    private static final String RANDOM_CHARS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private final UrlMappingRepository repository;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    public UrlShortenerService(UrlMappingRepository repository) {
        this.repository = repository;
    }

    public String shorten(String longUrl) {
        String shortCode = generateUniqueShortCode(longUrl);
        UrlMapping mapping = new UrlMapping(shortCode, longUrl);
        repository.save(mapping);
        return shortCode;
    }

    private String generateUniqueShortCode(String longUrl) {
        String fullHash = sha256Base64Url(longUrl);

        for (int len = INITIAL_CODE_LENGTH; len <= MAX_CODE_LENGTH; len++) {
            String candidate = fullHash.substring(0, len);
            if (!repository.existsByShortCode(candidate)) {
                return candidate;
            }
        }

        String candidate;
        do {
            candidate = fullHash.substring(0, MAX_CODE_LENGTH) + randomChar();
        } while (repository.existsByShortCode(candidate));

        return candidate;
    }

    private String sha256Base64Url(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 algorithm not available", e);
        }
    }

    private char randomChar() {
        int index = ThreadLocalRandom.current().nextInt(RANDOM_CHARS.length());
        return RANDOM_CHARS.charAt(index);
    }

    public String resolve(String shortCode) {
        return repository.findByShortCode(shortCode)
                .map(UrlMapping::getLongUrl)
                .orElseThrow(() -> new RuntimeException("No URL mapping found for short code: " + shortCode));
    }

    public String buildShortUrl(String shortCode) {
        return baseUrl + "/" + shortCode;
    }
}

