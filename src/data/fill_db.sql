-- Create table for inventory items
CREATE TABLE IF NOT EXISTS inventory_items (
                                               id INTEGER PRIMARY KEY,
                                               name TEXT NOT NULL,
                                               type TEXT NOT NULL,
                                               quantity INTEGER NOT NULL,
                                               weight REAL NOT NULL,
                                               description TEXT NOT NULL
);

-- Create table for floor items
CREATE TABLE IF NOT EXISTS floor_items (
                                           id INTEGER PRIMARY KEY,
                                           name TEXT NOT NULL,
                                           type TEXT NOT NULL,
                                           quantity INTEGER NOT NULL,
                                           weight REAL NOT NULL,
                                           description TEXT NOT NULL
);


-- Inserting items using INSERT OR IGNORE to skip duplicates
INSERT OR IGNORE INTO floor_items (id, name, type, quantity, weight, description) VALUES
(201, 'Iron Sword', 'Weapon', 1, 10.0, 'A shiny sword fit for a growing hero.'),
(202, 'Health Potion', 'Consumable', 5, 0.5, 'Restores delicious amount of health.'),
(203, 'Wooden Shield', 'Armor', 1, 8.0, 'A sturdy wooden shield for basic protection.'),
(204, 'Mana Potion', 'Consumable', 3, 0.3, 'Restores some magic power.'),
(205, 'Bow', 'Weapon', 1, 5.0, 'A basic bow for ranged attacks.'),
(206, 'Steel Dagger', 'Weapon', 1, 3.0, 'A lightweight dagger for swift attacks.'),
(207, 'Elixir of Strength', 'Consumable', 2, 0.7, 'Temporarily increases physical strength.'),
(208, 'Leather Armor', 'Armor', 1, 12.0, 'Basic protection for beginner adventurers.'),
(209, 'Torch', 'Tool', 3, 1.0, 'Provides light in dark places.'),
(210, 'Iron Helmet', 'Armor', 1, 5.0, 'Protects the head during battles.'),
(211, 'Crossbow', 'Weapon', 1, 7.5, 'A ranged weapon for precision attacks.'),
(212, 'Gold Coin Pouch', 'Currency', 1, 0.2, 'A small pouch filled with gold coins.'),
(213, 'Sapphire Ring', 'Accessory', 1, 0.1, 'A beautiful ring with a sapphire gemstone.'),
(214, 'Bread Loaf', 'Consumable', 4, 0.3, 'Restores a small amount of health.'),
(215, 'Rope', 'Tool', 1, 2.0, 'Useful for climbing or binding.'),
(216, 'Battle Axe', 'Weapon', 1, 15.0, 'A heavy weapon for devastating attacks.'),
(217, 'Healing Herb', 'Consumable', 6, 0.2, 'Used to craft potions or eaten raw for minor health recovery.'),
(218, 'Quiver of Arrows', 'Ammunition', 1, 1.5, 'A quiver filled with arrows for bows.'),
(219, 'Silver Chalice', 'Misc', 1, 1.0, 'A decorative chalice made of silver.'),
(220, 'Magic Scroll', 'Spell', 1, 0.1, 'Contains a spell for casting during battle.'),
(221, 'Chainmail Vest', 'Armor', 1, 20.0, 'Provides good protection against attacks.'),
(222, 'Dragon Scale', 'Rare', 1, 1.0, 'A scale from a dragon, highly valuable.'),
(223, 'Health Potion (Large)', 'Consumable', 2, 1.0, 'Restores a significant amount of health.'),
(878, 'eighitem', 'asd', 3, 20.0, 'asd');
