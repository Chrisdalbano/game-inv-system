package utils;

import javafx.scene.image.Image;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * IconHelper class provides a method to retrieve icons for different item categories.
 * Icons are loaded from the "/images" resource directory and cached in a map for efficient access.
 */

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

    /**
     * Retrieves the icon associated with the specified item category.
     * If no icon is found for the given category, a default icon is returned.
     *
     * @param category the category of the item
     * @return the Image object representing the icon for the category
     */

    public static Image getIconForCategory(String category) {
        return categoryIcons.getOrDefault(category, categoryIcons.get("Default"));
    }
}
