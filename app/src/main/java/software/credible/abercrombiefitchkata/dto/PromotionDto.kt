package software.credible.abercrombiefitchkata.dto

import com.google.gson.annotations.SerializedName

class PromotionDto {

    @SerializedName("button")
    var buttons: List<ButtonDto>? = null

    var description: String? = null

    var footer: String? = null

    @SerializedName("image")
    var imageUrl: String? = null

    var title: String? = null

}
