package com.noreabang.strawberryrabbit.domain.member.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class MemberUpdateRequest (
    @field:NotBlank(message = "The nickname cannot be blank.")
    @field:Size(min = 2, max = 32, message = "Nickname must be between 8 and 32")
    val nickname: String,

    @field:Size(min = 8, max = 16, message = "Password must be between 8 and 16")
    @field:NotBlank(message = "The password cannot be blank.")
    val password: String?
)