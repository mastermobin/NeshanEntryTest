package ir.mobin.test.model;

public class Shortcut {
    private String type;
    private int imageRes;
    private int bgColor;

    public Shortcut(String type, int imageRes, int bgColor) {
        this.type = type;
        this.imageRes = imageRes;
        this.bgColor = bgColor;
    }

    public String getType() {
        return type;
    }

    public int getImageRes() {
        return imageRes;
    }

    public int getBgColor() {
        return bgColor;
    }
}
