INSERT INTO site (name, city, address, postal_code, phone, email) VALUES
('Head Office', 'Rome', '10 Via Nazionale', 00184, '+39 06 1234567', 'central@company.com'),
('North Branch', 'Milan', '50 Corso Buenos Aires', 20124, '+39 02 7654321', 'north@company.com'),
('South Branch', 'Naples', '200 Via Toledo', 80134, '+39 081 9876543', 'south@company.com'),
('East Branch', 'Bari', '15 Via Sparano', 70121, '+39 080 1122334', 'east@company.com');


INSERT INTO vehicle (site_id, category, brand, model, transmission, color, seats, price) VALUES
(1, 'car', 'Fiat', 'Panda', 'manual', 'white', 5, 35.00),
(4, 'car', 'Volkswagen', 'Golf', 'automatic', 'black', 5, 50.00),
(2, 'scooter', 'Piaggio', 'Liberty 125', 'automatic', 'red', 2, 20.00),
(3, 'truck', 'Iveco', 'Daily', 'manual', 'white', 3, 80.00),
(2, 'car', 'Renault', 'Clio', 'manual', 'blue', 5, 38.00),
(3, 'scooter', 'Yamaha', 'Xmax 300', 'automatic', 'gray', 2, 25.00),
(2, 'truck', 'Mercedes', 'Sprinter', 'manual', 'silver', 3, 90.00),
(4, 'car', 'Tesla', 'Model 3', 'automatic', 'red', 5, 120.00);
