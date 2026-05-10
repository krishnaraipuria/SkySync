package com.example.note.Security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.asm.Type
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.yaml.snakeyaml.tokens.CommentToken
import java.util.Date
import javax.crypto.SecretKey


@Service
class JwtService(
    @Value("JWT_SECR_BASE64") private val jwtSecret: String,
) {
    private val secret = Keys.hmacShaKeyFor(java.util.Base64.getDecoder().decode(jwtSecret));
    private val accessTokenValdityMs = 15L * 60L * 1000L
    val refreshTokenValdityMs = 30 * 24 * 60 * 60 * 1000L

    private fun generateToken(
        userId: String,
        type: String,
        expiresIn: Long,
    ): String {
        val now = java.util.Date()
        val expiryDate: Date = Date(now.time + expiresIn)
        return Jwts.builder()
            .subject(userId)
            .claim("type", type)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(secret, Jwts.SIG.HS256)
            .compact()
    }

    fun generateAccessToken(userId: String): String {
        return generateToken(userId, "access", accessTokenValdityMs)
    }

    fun generateRefreshToken(userId: String): String {
        return generateToken(userId, "refresh", refreshTokenValdityMs)
    }
    fun valdidateRefreshToken(token: String): Boolean {
        val claims = pareseAllClaims(token)?: return false
        val tokenType = claims["type"] ?: return false
        return tokenType == "refresh"
    }
    fun getUserIdfromToken(token: String): String {
        val rawToken = if(token.startsWith("Bearer")) token.removePrefix("Bearer") else token
        val claims = pareseAllClaims(rawToken) ?: throw IllegalArgumentException("invalid token")
        return claims.subject
    }
    private  fun pareseAllClaims(token: String): Claims? {
        return try {
            Jwts.parser()
                .verifyWith(secret)
                .build()
                .parseSignedClaims(token)
                .payload
        } catch (e: Exception){
            null
        }
    }
}