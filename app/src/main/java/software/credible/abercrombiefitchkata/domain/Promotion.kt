package software.credible.abercrombiefitchkata.domain

import io.realm.RealmList
import io.realm.RealmObject
import software.credible.abercrombiefitchkata.dto.PromotionDto
import java.util.*

open class Promotion : RealmObject() {

    var id: String? = null
    var buttons: RealmList<Button>? = null
    var description: String? = null
    var footer: String? = null
    var imageUrl: String? = null
    var title: String? = null

    fun addButton(button: Button) {
        if (buttons == null) {
            this.buttons = RealmList()
        }
        buttons?.add(button)
    }

    companion object {

        /**
         * Necessary until users of this kotlin class convert
         * to Kotlin.
         */
        @JvmStatic
        fun fromDto(dto: PromotionDto): Promotion {
            val promotion = Promotion()
            promotion.id = UUID.randomUUID().toString()
            promotion.description = dto.description
            promotion.footer = dto.footer
            promotion.title = dto.title
            promotion.imageUrl = dto.imageUrl
            if (dto.buttons != null) {
                for (buttonDto in dto.buttons ?: emptyList()) {
                    promotion.addButton(Button.fromDto(buttonDto))
                }
            }
            return promotion
        }
    }
}
