package software.credible.abercrombiefitchkata.remote

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement

import java.lang.reflect.Type
import java.util.ArrayList

import software.credible.abercrombiefitchkata.dto.ButtonDto

/**
 * Buttons may come as a JSON Array or as a single Button.  This type adapter
 * allows PromotionDto to easily just get back a list of buttons, regardless of whether
 * a single Button was returned or an array of Buttons.
 *
 * Examples:
 * As a JSON Array
 * {
 * "promotions": [{
 * "button": {
 * "target": "https://m.abercrombie.com",
 * "title": "Shop Now"
 * },...],
 * ...
 * }
 * ...
 * }
 *
 * As a single element
 * {
 * "promotions": {
 * "button": {
 * "target": "https://m.hollisterco.com",
 * "title": "Shop Now"
 * },
 * ...
 * }
 * ...
 * }
 */
class ButtonTypeAdapter : JsonDeserializer<List<ButtonDto>> {

    override fun deserialize(json: JsonElement, typeOfT: Type, ctx: JsonDeserializationContext): List<ButtonDto> {

        val vals = ArrayList<ButtonDto>()

        if (json.isJsonArray) {
            for (e in json.asJsonArray) {
                vals.add(ctx.deserialize<Any>(e, ButtonDto::class.java) as ButtonDto)
            }

        } else if (json.isJsonObject) {
            vals.add(ctx.deserialize<Any>(json, ButtonDto::class.java) as ButtonDto)

        } else {
            throw RuntimeException("Unexpected JSON type: " + json.javaClass)
        }
        return vals
    }
}
