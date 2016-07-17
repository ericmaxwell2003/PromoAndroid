package software.credible.abercrombiefitchkata.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PromotionDto {

    @SerializedName("button")
    private List<ButtonDto> buttons;

    private String description;

    private String footer;

    @SerializedName("image")
    private String imageUrl;

    private String title;

    public List<ButtonDto> getButtons() {
        return buttons;
    }

    public void setButtons(List<ButtonDto> buttons) {
        this.buttons = buttons;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
