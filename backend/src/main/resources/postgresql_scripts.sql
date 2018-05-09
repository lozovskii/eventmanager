DROP TABLE IF EXISTS
"Message",
"Chat",
"Customer_WishList",
"Event_WishList",
"Customer_Item_Priority",
"Item_WishList",
"Item_Tag",
"Item",
"Tag",
"Customer_Event",
"Customer_Event_Priority",
"Customer_Event_Status",
"Event_Status",
"Event_Visibility",
"Event",
"Folder",
"Relationship",
"Customer",
"Relation_Status";

CREATE TABLE "Relation_Status"
(
  id 			smallserial PRIMARY KEY,
  name			varchar(20),
  description		text
);


CREATE TABLE "Customer"
(
  id 			uuid PRIMARY KEY DEFAULT uuid_generate_v1(),
  name 			varchar(20),
  second_name 		varchar(20),
  phone 		varchar(14),
  login 		varchar(20) NOT NULL UNIQUE,
  email 		varchar(40) NOT NULL UNIQUE,
  password 		text NOT NULL,
  isVerified 		boolean,
  token 		text,
  avatar		text,
  registration_date 	TIMESTAMP WITHOUT TIME ZONE
);

CREATE TABLE "Relationship"
(
  id			uuid PRIMARY KEY DEFAULT uuid_generate_v1(),
  sender_friend_id	uuid REFERENCES "Customer" (id) ON DELETE CASCADE,
  recipient_friend_id	uuid REFERENCES "Customer" (id) ON DELETE CASCADE,
  status		smallserial REFERENCES "Relation_Status" (id) ON DELETE CASCADE
);


CREATE TABLE "Folder"
(
  id			uuid PRIMARY KEY DEFAULT uuid_generate_v1(),
  customer_id		uuid REFERENCES "Customer" (id) ON DELETE CASCADE,
  name			varchar(20) NOT NULL,
  isShared		boolean
);

CREATE TABLE "Event_Status"
(
  id 			smallserial PRIMARY KEY,
  name			varchar(20) NOT NULL UNIQUE,
  description		text
);

CREATE TABLE "Event_Visibility"
(
  id 			smallserial PRIMARY KEY,
  name			varchar(20) NOT NULL UNIQUE,
  description		text
);

CREATE TABLE "Event"
(
  id 			uuid PRIMARY KEY DEFAULT uuid_generate_v1(),
  group_id		uuid DEFAULT uuid_generate_v1(),
  name			varchar(40) NOT NULL,
  folder_id 		uuid REFERENCES "Folder" (id) ON DELETE CASCADE,
  creator_id 		uuid REFERENCES "Customer" (id) ON DELETE CASCADE,
  start_time		TIMESTAMP WITHOUT TIME ZONE,
  end_time		TIMESTAMP WITHOUT TIME ZONE,
  description   varchar(1024),
  visibility		smallserial REFERENCES "Event_Visibility" (id) ON DELETE CASCADE,
  status		smallserial REFERENCES "Event_Status" (id) ON DELETE CASCADE
);

CREATE TABLE "Customer_Event_Status"
(
  id 			smallserial PRIMARY KEY,
  name			varchar(20) NOT NULL UNIQUE,
  description		text
);

CREATE TABLE "Customer_Event_Priority"
(
  id 			smallserial PRIMARY KEY,
  name			varchar(20) NOT NULL UNIQUE,
  description		text
);

CREATE TABLE "Customer_Event"
(
  id 			uuid PRIMARY KEY DEFAULT uuid_generate_v1(),
  event_id 		uuid REFERENCES "Event" (id) ON DELETE CASCADE,
  customer_id 		uuid REFERENCES "Customer" (id) ON DELETE CASCADE,
  start_date_notification TIMESTAMP WITHOUT TIME ZONE,
  frequency_value		smallint,
  priority		smallserial REFERENCES "Customer_Event_Priority" (id) ON DELETE CASCADE,
  status		smallserial REFERENCES "Customer_Event_Status" (id) ON DELETE CASCADE
);


CREATE TABLE "Tag"
(
  id 			uuid PRIMARY KEY DEFAULT uuid_generate_v1(),
  name			varchar(30) NOT NULL,
  count 		integer
);

CREATE TABLE "Item"
(
  id			uuid PRIMARY KEY DEFAULT uuid_generate_v1(),
  name			varchar(40) NOT NULL,
  description		varchar(1024),
  image			text,
  link			varchar(128)
);

CREATE TABLE "Item_Tag"
(
  id			uuid PRIMARY KEY DEFAULT uuid_generate_v1(),
  tag_id		uuid NOT NULL REFERENCES "Tag" (id) ON DELETE CASCADE,
  item_id		uuid NOT NULL REFERENCES "Item" (id) ON DELETE CASCADE
);

CREATE TABLE "Customer_WishList"
(
  id			uuid PRIMARY KEY DEFAULT uuid_generate_v1(),
  customer_id   uuid NOT NULL UNIQUE REFERENCES "Customer" (id) ON DELETE CASCADE
);

CREATE TABLE "Customer_Item_Priority"
(
  id 			smallserial PRIMARY KEY,
  name			varchar(20) NOT NULL UNIQUE,
  description		text
);

CREATE TABLE "Item_WishList"
(
  id			uuid PRIMARY KEY DEFAULT uuid_generate_v1(),
  item_id		uuid REFERENCES "Item" (id) ON DELETE CASCADE,
  booker_customer_id	uuid REFERENCES "Customer_WishList" (customer_id) ON DELETE CASCADE,
  priority 		smallserial NOT NULL REFERENCES "Customer_Item_Priority" (id) ON DELETE CASCADE
);


CREATE TABLE "Event_WishList"
(
  id			uuid PRIMARY KEY DEFAULT uuid_generate_v1(),
  event_id		uuid NOT NULL REFERENCES "Event" (id) ON DELETE CASCADE,
  item_wishlist_id		uuid NOT NULL REFERENCES "Item_WishList" (id) ON DELETE CASCADE
);


CREATE TABLE "Chat"
(
  id			uuid PRIMARY KEY DEFAULT uuid_generate_v1(),
  event_id		uuid NOT NULL REFERENCES "Event" (id) ON DELETE CASCADE,
  withOwner		boolean
);

CREATE TABLE "Message"
(
  id			uuid PRIMARY KEY DEFAULT uuid_generate_v1(),
  chat_id		uuid NOT NULL REFERENCES "Chat" (id) ON DELETE CASCADE,
  author_id		uuid NOT NULL REFERENCES "Customer" (id) ON DELETE CASCADE,
  content		text NOT NULL,
  date			TIMESTAMP WITHOUT TIME ZONE NOT NULL
);
