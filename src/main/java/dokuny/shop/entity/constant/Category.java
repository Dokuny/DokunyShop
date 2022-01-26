package dokuny.shop.entity.constant;

public enum Category {
    MEN,WOMEN,KIDS,ACCESSORIES;

    public static Category parseCategory(String categoryNm) {
        String category = categoryNm.toUpperCase();
        if (category.equals(MEN.name())) {
            return MEN;
        } else if (category.equals(WOMEN.name())) {
            return WOMEN;
        } else if (category.equals(KIDS.name())) {
            return KIDS;
        } else if (category.equals(ACCESSORIES.name())) {
            return ACCESSORIES;
        } else {
            return null;
        }
    }
}
