package software.credible.abercrombiefitchkata.dto;

import com.google.gson.annotations.SerializedName;

public class ButtonDto {

    @SerializedName("target")
    private String targetUrl;

    private String title;

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
}
