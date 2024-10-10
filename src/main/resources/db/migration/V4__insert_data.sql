INSERT INTO boxes (name, form, symbol)
VALUES ('один', '1', '1'),
       ('два', '22', '2'),
       ('три', '333', '3'),
       ('четыре', '4444', '4'),
       ('пять', '55555', '5');

INSERT INTO boxes (name, form, symbol)
VALUES ('шесть', '666\n666', '6'),
       ('семь', '777 \n7777', '7'),
       ('восемь', '8888\n8888', '8'),
       ('девять', '999\n999\n999', '9');

INSERT INTO file_entities (file_name, file_type)
VALUES ('trucks.json', 'TRUCK_FILE BOX_FILE');

INSERT INTO file_entities (file_name, file_type)
VALUES ('boxes.json', 'BOX_FILE');