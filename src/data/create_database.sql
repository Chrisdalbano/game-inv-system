CREATE TABLE items (
                       id INTEGER PRIMARY KEY,
                       name TEXT NOT NULL,
                       type TEXT NOT NULL,
                       quantity INTEGER NOT NULL,
                       weight REAL NOT NULL,
                       description TEXT NOT NULL
);

INSERT INTO items (id, name, type, quantity, weight, description) VALUES
                                                                      (201, 'Iron Sword', 'Weapon', 1, 10.0, 'A shiny sword fit for a growing hero.'),
                                                                      (202, 'Health Potion', 'Consumable', 5, 0.5, 'Restores delicious amount of health.'),
                                                                      (203, 'Wooden Shield', 'Armor', 1, 8.0, 'A sturdy wooden shield for basic protection.');
