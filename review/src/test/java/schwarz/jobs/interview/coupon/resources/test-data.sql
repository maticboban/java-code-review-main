DROP TABLE IF EXISTS coupons;
 
CREATE TABLE coupons (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  code VARCHAR(250) NOT NULL,
  discount NUMBER(10,2) NOT NULL,
  minBasketValue NUMBER(10,2) DEFAULT NULL,
  constraint uq1 UNIQUE (code)
);
 
INSERT INTO coupons (code, discount, minBasketValue) VALUES
  ('TEST100', 10.00, 50.00),
  ('TEST200', 15.00, 100.00),
  ('TEST300', 20.00, 200.00);