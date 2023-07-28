insert into users(id,first_name,last_name,email,password,image,role)
values (1,'Abdumalik','Turatbek uulu','asanbekovmalik2@gmail.com', '$2a$12$6RFdM5bwOFsWmbrD8Z4u5u/VcEyRTebNBbFcHbMEcOe1MupujUrPS',  'https://ichef.bbci.co.uk/news/976/cpsprodpb/3EE0/production/_121269061_getty.jpg','ADMIN'),
       (2,'Matmusa','Abduvohob uulu','admin@gmail.com', '$2a$12$sXb9HkBhXH8IpmYY05Dbm.UFYN1nPi8Gghye68nWreDd3iaeVGW9a',  'https://www.brend.uz/upload_files/news/567135ea55997.jpg','ADMIN'),
       (3,'Lira','Kanaatova','lira.kanaatova.ch@gmail.com', '$2a$12$N8/R3Q04xhEAhZ1vWcaQquLn3nDE5V8xwPApuBndhKNUUCr.mljf.',  'https://images.squarespace-cdn.com/content/v1/61b76cc1f6acc75c4a875e90/ad0f8b40-9936-4b7a-931b-f4f9b2da0b47/output-onlinepngtools.png','ADMIN'),
       (4,'Adilet','Islambek uulu','adilet@gmail.com', '$2a$12$xYXRx6kjHtxqb0sV1jLrh.WrcfmoVazuwqLY1YmVjHlGjcHroICVO',  'https://biogr.net/wp-content/uploads/2022/02/63463463.jpg','ADMIN'),
       (5,'Manas','Abdugani uulu','manas@gmail.com', '$2a$12$egcK6uZ5RlHPgEzEEczyM.VVX33yCsgyH2Kdw4m.lJk7Bl2pnE39.',  'https://kai.kg/public/images/2021/12/1638521287.png','ADMIN');

insert into work_spaces(id,name,admin_id)
values (1,'Taigan',1),
       (2,'LMS',2),
       (3,'Gadgetarium',3),
       (4,'AirBnb',4),
       (5,'MedCheck',5);

insert into boards(id,title,back_ground,work_space_id)
values (1,'word','img',1),
       (2,'title','img',2),
       (3,'word','img',3),
       (4,'title','img',4),
       (5,'word','img',5);

insert into favorites(id,user_id,board_id,work_space_id)
values (1,5,5,5),
       (2,4,4,4),
       (3,3,3,3),
       (4,2,2,2),
       (5,1,1,1);

insert into user_work_space_roles(id,role,user_id,work_space_id)
values (1,'ADMIN',5,5),
       (2,'MEMBER',4,4),
       (3,'ADMIN',3,3),
       (4,'ADMIN',2,2),
       (5,'MEMBER',1,1);

insert into columns(id,title,is_archive,board_id)
values (1,'Done',false,1),
       (2,'Attention',false,2),
       (3,'Done',false,3),
       (4,'Attention',false,4),
       (5,'Done',false,5);

insert into cards(id,title,description,is_archive,created_date,column_id)
values (1,'title','words',false,now(),1),
       (2,'word','words',false,now(),2),
       (3,'title','words',false,now(),3),
       (4,'word','words',false,now(),4),
       (5,'title','words',false,now(),5);

insert into notifications(id,text,image,type,is_read,created_date,card_id)
values (1,'batyraak bol','img','MOVE',false,now(),2),
       (2,'vnimanie','img','MOVE',false,now(),1),
       (3,'vas dobavili','img','ASSIGN',false,now(),3),
       (4,'pravilno','img','REMINDER',false,now(),4),
       (5,'normalno','img','REMINDER',false,now(),5);

insert into labels(id,color,label_name)
values (1,'Green','Done'),
       (2,'Red','Attention'),
       (3,'Blue','Attention'),
       (4,'Orange','Attention'),
       (5,'Green','Done');

insert into check_lists(id,percent,description,card_id)
values (1,10,'writing',1),
       (2,20,'character',2),
       (3,30,'reading',3),
       (4,40,'writing',4),
       (5,70,'task',5);

insert into comments(id,comment,created_date,card_id,user_id)
  values (1,'Kachan butot',now(),1,1),
         (2,'Tuura emesko brat',now(),2,2),
         (3,'tutorial please',now(),3,3),
         (4,'Harosh',now(),4,4),
         (5,'Good job',now(),5,5);

insert into estimations(id,reminder_type,start_date,due_date,time,card_id,notification_id)
values (1,'NONE','2023-07-10T10:30:00+03:00','2023-07-11T10:30:00+03:00','2023-07-11T10:30:00+03:00',1,1),
       (2,'FIVE_MINUTE','2023-07-09T10:30:00+03:00','2023-07-10T10:30:00+03:00','2023-07-10T10:30:00+03:00',2,2),
       (3,'TEN_MINUTE','2023-07-08T10:30:00+03:00','2023-07-09T10:30:00+03:00','2023-07-09T10:30:00+03:00',3,3),
       (4,'FIFTEEN_MINUTE','2023-07-07T10:30:00+03:00','2023-07-08T10:30:00+03:00','2023-07-08T10:30:00+03:00',4,4),
       (5,'THIRD_MINUTE','2023-07-06T10:30:00+03:00','2023-07-07T10:30:00+03:00','2023-07-07T10:30:00+03:00',5,5);

insert into items(id,is_done,title,check_list_id)
values (1,false,'word',1),
       (2,false,'read',2),
       (3,false,'word',3),
       (4,false,'read',4),
       (5,false,'word',5);

insert into attachments(id,created_at,document_link,card_id)
values (1,now(),'link',1),
       (2,now(),'link',2),
       (3,now(),'link',3),
       (4,now(),'link',4),
       (5,now(),'link',5);

insert into users_work_spaces(work_spaces_id,users_id)
values (1,1),
       (2,2),
       (3,3),
       (4,4),
       (5,5);

insert into notifications_users(notifications_id,users_id)
values (1,1),
       (2,2),
       (3,3),
       (4,4),
       (5,5);

insert into labels_cards(cards_id,labels_id)
values (1,1),
       (2,2),
       (3,3),
       (4,4),
       (5,5);

insert into columns_users(columns_id,users_id)
values (1,1),
       (2,2),
       (3,3),
       (4,4),
       (5,5);

insert into cards_users(cards_id,users_id)
values (1,1),
       (2,2),
       (3,3),
       (4,4),
       (5,5);

insert into boards_users(boards_id,users_id)
values (1,1),
       (2,2),
       (3,3),
       (4,4),
       (5,5);