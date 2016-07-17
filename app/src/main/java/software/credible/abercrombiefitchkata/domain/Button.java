package software.credible.abercrombiefitchkata.domain;

import java.util.UUID;

import io.realm.RealmObject;
import software.credible.abercrombiefitchkata.dto.ButtonDto;
import software.credible.abercrombiefitchkata.dto.PromotionDto;

public class Button extends RealmObject {

    private String id;
    private String targetUrl;
    private String title;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static Button fromDto(ButtonDto dto) {
        Button button = new Button();
        button.setId(UUID.randomUUID().toString());
        button.setTitle(dto.getTitle());
        button.setTargetUrl(dto.getTargetUrl());
        return button;
    }

}
