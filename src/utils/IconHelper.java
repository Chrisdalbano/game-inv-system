package utils;

import javafx.scene.image.Image;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class IconHelper {
    private static final Map<String, Image> categoryIcons = new HashMap<>();

    static {
        // Load icons safely
        categoryIcons.put("Weapon", new Image(Objects.requireNonNull(IconHelper.class.getResource("/images/weapon.png"), "Weapon icon not found").toExternalForm()));
        categoryIcons.put("Armor", new Image(Objects.requireNonNull(IconHelper.class.getResource("/images/armor.png"), "Armor icon not found").toExternalForm()));
        categoryIcons.put("Consumable", new Image(Objects.requireNonNull(IconHelper.class.getResource("/images/consumable.png"), "Consumable icon not found").toExternalForm()));
        categoryIcons.put("Scroll", new Image(Objects.requireNonNull(IconHelper.class.getResource("/images/scroll.png"), "Scroll icon not found").toExternalForm()));
        categoryIcons.put("Misc", new Image(Objects.requireNonNull(IconHelper.class.getResource("/images/misc.png"), "Misc icon not found").toExternalForm()));
        categoryIcons.put("Default", new Image(Objects.requireNonNull(IconHelper.class.getResource("/images/default.png"), "Default icon not found").toExternalForm()));
    }

    public static Image getIconForCategory(String category) {
        return categoryIcons.getOrDefault(category, categoryIcons.get("Default"));
    }
}
