create table PRODUCT(
  ID bigint not null AUTO_INCREMENT,
  NAME varchar(100) not null,
  PRIMARY KEY ( ID )

);
create table OFFER(
  ID bigint not null AUTO_INCREMENT,
  VALID_FROM TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRICE numeric (5,2),
  PRODUCT_ID  int not null,
  PRIMARY KEY ( ID ),
  CONSTRAINT fk_PRODUCT_ID_OFFER
  FOREIGN KEY (PRODUCT_ID)
  REFERENCES PRODUCT(ID)
);

create table SIZE(
  SIZE_ID bigint not null AUTO_INCREMENT,
  SIZE varchar(1) not null,
  AVAILABILITY boolean,
  LAST_UPDATED TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRODUCT_ID int not null,
  PRIMARY KEY ( SIZE_ID ),
  CONSTRAINT fk_PRODUCT_ID_SIZE
  FOREIGN KEY (PRODUCT_ID)
  REFERENCES PRODUCT(ID)
 
);




