package ua.edu.chnu.labo3.domain.usecase

import ua.edu.chnu.labo3.data.AboutVisitInfo
import ua.edu.chnu.labo3.data.AppSettingsRepository
import ua.edu.chnu.labo3.data.SystemInfo
import ua.edu.chnu.labo3.data.SystemInfoRepository

/** Collects the system information and logs it. */
class GetSystemInfoUseCase(private val repository: SystemInfoRepository) {
    operator fun invoke(): SystemInfo = repository.getSystemInfo().also { repository.logSystemInfo() }
}

/** Records one visit of the About screen and returns the updated statistics. */
class RegisterAboutVisitUseCase(private val repository: AppSettingsRepository) {
    operator fun invoke(): AboutVisitInfo = repository.registerAboutOpened()
}
