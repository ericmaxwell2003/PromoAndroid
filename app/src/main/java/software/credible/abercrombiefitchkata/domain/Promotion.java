package software.credible.abercrombiefitchkata.domain;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import software.credible.abercrombiefitchkata.dto.ButtonDto;
import software.credible.abercrombiefitchkata.dto.PromotionDto;

public class Promotion extends RealmObject {

    private String id;
    private RealmList<Button> buttons;
    private String description;
    private String footer;
    private String imageUrl;
    private String title;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RealmList<Button> getButtons() {
        return buttons;
    }

    public void setButtons(RealmList<Button> buttons) {
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

    public void addButton(Button button) {
        if(getButtons() == null) {
            this.buttons = new RealmList<>();
        }
        getButtons().add(button);
    }

    public static Promotion fromDto(PromotionDto dto) {
        Promotion promotion = new Promotion();
        promotion.setId(UUID.randomUUID().toString());
        promotion.setDescription(dto.getDescription());
        promotion.setFooter(dto.getFooter());
        promotion.setTitle(dto.getTitle());
        promotion.setImageUrl(dto.getImageUrl());
        for(ButtonDto buttonDto : dto.getButtons()) {
            promotion.addButton(Button.fromDto(buttonDto));
        }
        return promotion;
    }
}
