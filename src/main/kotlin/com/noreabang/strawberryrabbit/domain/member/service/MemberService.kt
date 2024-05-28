package com.noreabang.strawberryrabbit.domain.member.service

import com.noreabang.strawberryrabbit.domain.member.repository.MemberRepository
import com.noreabang.strawberryrabbit.infra.secutiry.exception.CustomJwtException
import com.noreabang.strawberryrabbit.infra.secutiry.util.JwtUtil
import org.springframework.stereotype.Service
import java.util.*

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val jwtUtil: JwtUtil
) {
    fun refresh(authHeader: String, refreshToken: String): Map<String, Any> {
        if (refreshToken == null) {
            throw CustomJwtException("NULL_REFRESH")
        }

        if (authHeader == null || authHeader.length < 7) {
            throw CustomJwtException("INVALID_STRING")
        }

        val accessToken = authHeader.substring(7)

        if (!checkExpiredToken(accessToken)) { // AccessToken이 만료되지 않았다면 그대로 반환
            return mapOf("accessToken" to accessToken, "refreshToken" to refreshToken)
        }

        val claims = jwtUtil.validateToken(refreshToken)
        val newAccessToken = jwtUtil.generateToken(claims, 10) // 10분
        val newRefreshToken =
            if (checkTime(claims["exp"] as Int)) jwtUtil.generateToken(claims, 60 * 24) else refreshToken

        return mapOf("accessToken" to newAccessToken, "refreshToken" to newRefreshToken)
    }

    fun checkTime(exp: Int): Boolean { // 1시간 미만 여부
        val expDate = Date(exp.toLong() * 1000) // JWT exp를 날짜로 변환
        val gap = expDate.time - System.currentTimeMillis() // 현재 시간과 차이 계산(ms)
        val leftMin = gap / (1000 * 60)
        println("checkTime - expDate: $expDate, gap: $gap, leftMin: $leftMin")

        return leftMin < exp
    }

    fun checkExpiredToken(token: String): Boolean {
        try {
            jwtUtil.validateToken(token)
        } catch (e: CustomJwtException) {
            if (e.message == "Expired") {
                return true
            }
        }
        return false
    }
}