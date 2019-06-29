package ir.mobin.test.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ir.mobin.test.R;

public class Utils {
    private static final Pattern DIGIT_OR_LINK_PATTERN =
            Pattern.compile("(\\d|https?:[\\w_/+%?=&.]+)");

    private static final Map<String, String> PERSIAN_DIGITS = new HashMap<>();
    private static final Map<String, String> PERSIAN_TYPES = new HashMap<>();
    private static final Map<String, Integer> IMAGE_DRAWABLES = new HashMap<>();

    static {
        IMAGE_DRAWABLES.put("restaurant", R.drawable.ic_restaurant);
        IMAGE_DRAWABLES.put("gas_station", R.drawable.ic_gas_station);
        IMAGE_DRAWABLES.put("wc", R.drawable.ic_wc);
        IMAGE_DRAWABLES.put("road_helal_ahmar", R.drawable.ic_helal_ahmar);
        IMAGE_DRAWABLES.put("parking", R.drawable.ic_parking);
        IMAGE_DRAWABLES.put("mosque", R.drawable.ic_mosque);
        IMAGE_DRAWABLES.put("atm", R.drawable.ic_atm);
        IMAGE_DRAWABLES.put("fast_food", R.drawable.ic_fastfood);
        IMAGE_DRAWABLES.put("shopping_mall", R.drawable.ic_store);
        IMAGE_DRAWABLES.put("pharmacy", R.drawable.ic_pharmacy);
        IMAGE_DRAWABLES.put("cafe", R.drawable.ic_cafe);
        IMAGE_DRAWABLES.put("hospital", R.drawable.ic_hospital);
        IMAGE_DRAWABLES.put("car_wash", R.drawable.ic_car_wash);
        IMAGE_DRAWABLES.put("hotel", R.drawable.ic_hotel);
        IMAGE_DRAWABLES.put("misc", R.drawable.ic_misc);

        PERSIAN_DIGITS.put("0", "۰");
        PERSIAN_DIGITS.put("1", "۱");
        PERSIAN_DIGITS.put("2", "۲");
        PERSIAN_DIGITS.put("3", "۳");
        PERSIAN_DIGITS.put("4", "۴");
        PERSIAN_DIGITS.put("5", "۵");
        PERSIAN_DIGITS.put("6", "۶");
        PERSIAN_DIGITS.put("7", "۷");
        PERSIAN_DIGITS.put("8", "۸");
        PERSIAN_DIGITS.put("9", "۹");

        PERSIAN_TYPES.put("restaurant", "رستوران");
        PERSIAN_TYPES.put("gas_station", "مرکز سوخت");
        PERSIAN_TYPES.put("wc", "سرویس بهداشتی");
        PERSIAN_TYPES.put("road_helal_ahmar", "پایگاه امداد");
        PERSIAN_TYPES.put("parking", "پارکینگ");
        PERSIAN_TYPES.put("mosque", "مسجد");
        PERSIAN_TYPES.put("atm", "خودپرداز");
        PERSIAN_TYPES.put("fast_food", "فست‌فود");
        PERSIAN_TYPES.put("shopping_mall", "مرکز خرید");
        PERSIAN_TYPES.put("pharmacy", "داروخانه");
        PERSIAN_TYPES.put("cafe", "کافی‌شاپ");
        PERSIAN_TYPES.put("hospital", "بیمارستان");
        PERSIAN_TYPES.put("car_wash", "کارواش");
        PERSIAN_TYPES.put("hotel", "هتل");

    }

    public static String persianDigits(String s) {
        StringBuffer sb = new StringBuffer();
        Matcher m = DIGIT_OR_LINK_PATTERN.matcher(s);
        while (m.find()) {
            String t = m.group(1);
            if (t.length() == 1) {
                // Digit.
                t = PERSIAN_DIGITS.get(t);
            }
            m.appendReplacement(sb, t);
        }
        m.appendTail(sb);
        return sb.toString();
    }

    public static int getTypeRes(String type){
        if(IMAGE_DRAWABLES.containsKey(type))
            return IMAGE_DRAWABLES.get(type);
        return R.drawable.ic_misc;
    }

    public static String translateTypes(String type) {
        if (PERSIAN_TYPES.containsKey(type))
            return PERSIAN_TYPES.get(type);
        return "متفرقه";
    }
}
