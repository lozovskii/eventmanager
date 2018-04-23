DROP TABLE IF EXISTS
"Message",
"Chat",
"Customer_WishList",
"Event_WishList",
"Item_WishList",
"Item_Tag",
"Item",
"Tag",
"Customer_Event",
"Customer_Event_Priority",
"Customer_Event_Status",
"Event_Status",
"Event_Frequency",
"Event_Visibility",
"Event",
"Folder",
"Customer_Role",
"Role",
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
  avatar		bytea,
  registration_date 	TIMESTAMP WITHOUT TIME ZONE
);

CREATE TABLE "Relationship"
(
  id			uuid PRIMARY KEY DEFAULT uuid_generate_v1(),
  sender_friend_id	uuid REFERENCES "Customer" (id) ON DELETE CASCADE,
  recipient_friend_id	uuid REFERENCES "Customer" (id) ON DELETE CASCADE,
  status		smallserial REFERENCES "Relation_Status" (id) ON DELETE CASCADE
);

CREATE TABLE "Role"
(
  id 			smallserial PRIMARY KEY,
  name	 		varchar(20) NOT NULL UNIQUE,
  description 		text
);

CREATE TABLE "Customer_Role"
(
  id 			uuid PRIMARY KEY DEFAULT uuid_generate_v1(),
  customer_id 		uuid REFERENCES "Customer" (id) ON DELETE CASCADE,
  role_id 		smallserial  REFERENCES "Role" (id) ON DELETE CASCADE
);


CREATE TABLE "Folder"
(
  id			uuid PRIMARY KEY DEFAULT uuid_generate_v1(),
  customer_id		uuid REFERENCES "Customer" (id) ON DELETE CASCADE,
  name			varchar(20) NOT NULL
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
  folder_id 		uuid REFERENCES "Folder" (id) ON DELETE CASCADE,
  creator_id 		uuid REFERENCES "Customer" (id) ON DELETE CASCADE,
  start_time		TIMESTAMP WITHOUT TIME ZONE,
  end_time		TIMESTAMP WITHOUT TIME ZONE,
  description   varchar(1024),
  name			varchar(40) NOT NULL,
  priority		varchar(20),
  visibility		smallserial REFERENCES "Event_Visibility" (id) ON DELETE CASCADE,
  frequency_value VARCHAR(20),
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
  frequency		varchar(20),
  priority		smallserial REFERENCES "Customer_Event_Priority" (id) ON DELETE CASCADE,
  status		smallserial REFERENCES "Customer_Event_Status" (id) ON DELETE CASCADE
);


CREATE TABLE "Tag"
(
  id 			uuid PRIMARY KEY DEFAULT uuid_generate_v1(),
  name			varchar(40) NOT NULL
);

CREATE TABLE "Item"
(
  id			uuid PRIMARY KEY DEFAULT uuid_generate_v1(),
  name			varchar(40) NOT NULL,
  description		varchar(1024),
  image			bytea,
  link			varchar(256)
);

CREATE TABLE "Item_Tag"
(
  id			uuid PRIMARY KEY DEFAULT uuid_generate_v1(),
  tag_id		uuid NOT NULL REFERENCES "Tag" (id) ON DELETE CASCADE,
  item_id		uuid NOT NULL REFERENCES "Item" (id) ON DELETE CASCADE
);

CREATE TABLE "Item_WishList"
(
  id			uuid PRIMARY KEY DEFAULT uuid_generate_v1(),
  item_id		uuid REFERENCES "Item" (id) ON DELETE CASCADE,
  wishlist_id		uuid,
  booker_customer_id	uuid
);

CREATE TABLE "Event_WishList"
(
  id			uuid PRIMARY KEY DEFAULT uuid_generate_v1(),
  event_id		uuid NOT NULL REFERENCES "Event" (id) ON DELETE CASCADE
);

CREATE TABLE "Customer_WishList"
(
  id			uuid PRIMARY KEY DEFAULT uuid_generate_v1(),
  customer_id		uuid NOT NULL REFERENCES "Customer" (id) ON DELETE CASCADE
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

