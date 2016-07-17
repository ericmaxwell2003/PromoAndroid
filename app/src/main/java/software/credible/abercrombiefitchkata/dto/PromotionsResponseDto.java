package software.credible.abercrombiefitchkata.dto;

import java.util.List;

public class PromotionsResponseDto {

    private List<PromotionDto> promotions;

    public List<PromotionDto> getPromotions() {
        return promotions;
    }

    public void setPromotions(List<PromotionDto> promotions) {
        this.promotions = promotions;
    }
}
