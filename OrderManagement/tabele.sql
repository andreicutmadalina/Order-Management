drop table OrderItem;
drop table FinalOrder;

drop table Product;
drop table Client;

CREATE TABLE Product (
	id int NOT NULL,
	nume longtext NOT NULL,
	price float NOT NULL,
	stock float NOT NULL,
	deleted int NOT NULL );
    
CREATE TABLE Client (
	id int NOT NULL,
	nume longtext NOT NULL,
	address longtext NOT NULL,
	deleted int NOT NULL );
		
CREATE TABLE FinalOrder (
	id int NOT NULL,
	idClient int NOT NULL,
	totalPrice float NOT NULL,
	deleted int NOT NULL );
	
CREATE TABLE OrderItem (
	idOrder int NOT NULL,
	idProduct int NOT NULL,
	quantity float NOT NULL,
	deleted int NOT NULL );
	
ALTER TABLE Client
ADD CONSTRAINT client_pk PRIMARY KEY(id);

ALTER TABLE Product
ADD CONSTRAINT product_pk PRIMARY KEY(id);
	
ALTER TABLE FinalOrder
ADD CONSTRAINT order_pk PRIMARY KEY(id);

ALTER TABLE FinalOrder
ADD CONSTRAINT order_client_fk FOREIGN KEY(idClient)  REFERENCES Client(id);

ALTER TABLE OrderItem
ADD CONSTRAINT orderItem_pk PRIMARY KEY(idOrder, idProduct);

ALTER TABLE OrderItem
ADD CONSTRAINT orderItem_order_fk FOREIGN KEY(idOrder)  REFERENCES FinalOrder(id);

ALTER TABLE OrderItem
ADD CONSTRAINT orderItem_product_fk FOREIGN KEY(idProduct)  REFERENCES Product(id);

















	
	