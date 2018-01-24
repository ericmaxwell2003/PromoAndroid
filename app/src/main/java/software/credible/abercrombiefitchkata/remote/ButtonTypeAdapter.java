package software.credible.abercrombiefitchkata.remote;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import software.credible.abercrombiefitchkata.dto.ButtonDto;

/**
 * Buttons may come as a JSON Array or as a single Button.  This type adapter
 * allows PromotionDto to easily just get back a list of buttons, regardless of whether
 * a single Button was returned or an array of Buttons.
 *
 * Examples:
 * As a JSON Array
 * {
 *   "promotions": [{
 *       "button": {
 *           "target": "https://m.abercrombie.com",
 *           "title": "Shop Now"
 *       },...],
 *       ...
 *   }
 *   ...
 *  }
 *
 * As a single element
 * {
 *   "promotions": {
 *       "button": {
 *           "target": "https://m.hollisterco.com",
 *           "title": "Shop Now"
 *       },
 *       ...
 *   }
 *   ...
 *  }
 */
public class ButtonTypeAdapter implements JsonDeserializer<List<ButtonDto>> {

    public List<ButtonDto> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext ctx) {

        List<ButtonDto> vals = new ArrayList<>();

        if (json.isJsonArray()) {
            for (JsonElement e : json.getAsJsonArray()) {
                vals.add((ButtonDto) ctx.deserialize(e, ButtonDto.class));
            }

        } else if (json.isJsonObject()) {
            vals.add((ButtonDto) ctx.deserialize(json, ButtonDto.class));

        } else {
            throw new RuntimeException("Unexpected JSON type: " + json.getClass());
        }
        return vals;
    }
}
