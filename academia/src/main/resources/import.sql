/* Populate tables */
INSERT INTO clients (id, name, surname, email, phone_number, created) VALUES(1, 'Andres', 'Guzman', 'profesor@bolsadeideas.com', '954324512', '2017-08-01');
INSERT INTO clients (id, name, surname, email, phone_number, created) VALUES(2, 'John', 'Doe', 'john.doe@gmail.com', '954324512', '2017-08-02');
INSERT INTO clients (id, name, surname, email, phone_number, created) VALUES(3, 'Linus', 'Torvalds', 'linus.torvalds@gmail.com', '954324512', '2017-08-03');

/* Populate tables */
INSERT INTO students (name, surname, email, date_birth, created, photo, client_id) VALUES('Fernando', 'Fuentes Perez', 'fernandofuentesperez@gmail.com', '1988-08-23', '2019-04-23', '', 1);
INSERT INTO students (name, surname, email, date_birth, created, photo, client_id) VALUES('Kirsty', 'Brown', 'kirsty.a.d.brown@gmail.com', '1989-11-21', '2019-04-23', '', 1);
INSERT INTO students (name, surname, email, date_birth, created, photo, client_id) VALUES('Pedro', 'Martin', 'pedrom@gmail.com', '1988-08-23', '2019-04-23', '', 1);
INSERT INTO students (name, surname, email, date_birth, created, photo, client_id) VALUES('Janie', 'Roe', 'janie.roe@gmail.com','1988-08-23', '2017-08-21', '', 2);
INSERT INTO students (name, surname, email, date_birth, created, photo, client_id) VALUES('John', 'Smith', 'john.smith@gmail.com', '1977-08-22', '2019-04-23', '', 2);

/* Creamos algunas facturas */
INSERT INTO invoices (description, observation, client_id, created) VALUES('Factura equipos de oficina', null, 1, NOW());
INSERT INTO invoices_items (quantity, invoice_id, product_id) VALUES(1, 1, 1);
INSERT INTO invoices_items (quantity, invoice_id, product_id) VALUES(2, 1, 4);
INSERT INTO invoices_items (quantity, invoice_id, product_id) VALUES(1, 1, 5);
INSERT INTO invoices_items (quantity, invoice_id, product_id) VALUES(1, 1, 7);

INSERT INTO invoices (description, observation, client_id, created) VALUES('Factura Bicicleta', 'Alguna nota importante!', 1, NOW());
INSERT INTO invoices_items (quantity, invoice_id, product_id) VALUES(3, 2, 6);

/* Populate tabla productos */
INSERT INTO products (name, price, created) VALUES('Panasonic Pantalla LCD', 259990, NOW());
INSERT INTO products (name, price, created) VALUES('Sony Camara digital DSC-W320B', 123490, NOW());
INSERT INTO products (name, price, created) VALUES('Apple iPod shuffle', 1499990, NOW());
INSERT INTO products (name, price, created) VALUES('Sony Notebook Z110', 37990, NOW());
INSERT INTO products (name, price, created) VALUES('Hewlett Packard Multifuncional F2280', 69990, NOW());
INSERT INTO products (name, price, created) VALUES('Bianchi Bicicleta Aro 26', 69990, NOW());
INSERT INTO products (name, price, created) VALUES('Mica Comoda 5 Cajones', 299990, NOW());