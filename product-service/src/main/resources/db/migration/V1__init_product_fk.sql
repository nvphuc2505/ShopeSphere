ALTER TABLE product
    ADD CONSTRAINT fk_category_id FOREIGN KEY (category_id) INT REFERENCES category(id);


-- NUMERIC(precision, scale)
--        (Tổng số chữ số (cả trước và sau dấu thập phân), Số chữ số nằm sau dấu thập phân)
  -- hoặc
--  DECIMAL(precision, scale)
