package software.credible.abercrombiefitchkata.domain

import java.util.UUID

import io.realm.RealmObject
import software.credible.abercrombiefitchkata.dto.ButtonDto

open class Button : RealmObject() {

    var id: String? = null
    var targetUrl: String? = null
    var title: String? = null

    companion object {

        fun fromDto(dto: ButtonDto): Button {
            val button = Button()
            button.id = UUID.randomUUID().toString()
            button.title = dto.title
            button.targetUrl = dto.targetUrl
            return button
        }
    }

}
