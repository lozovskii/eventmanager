INSERT INTO "Relation_Status" (name) VALUES ('REQUEST');
INSERT INTO "Relation_Status" (name) VALUES ('ACCEPTED');
INSERT INTO "Relation_Status" (name) VALUES ('DELETED');

INSERT INTO "Role" (name) VALUES ('USER');
INSERT INTO "Role" (name) VALUES ('ADMIN');

INSERT INTO "Event_Status" (name) VALUES ('NON - OCCURRED');
INSERT INTO "Event_Status" (name) VALUES ('NOW');
INSERT INTO "Event_Status" (name) VALUES ('OCCURRED');

INSERT INTO "Event_Visibility"(name) VALUES ('PUBLIC');
INSERT INTO "Event_Visibility" (name) VALUES ('FOR FRIENDS ONLY');
INSERT INTO "Event_Visibility" (name) VALUES ('PRIVATE');

INSERT INTO "Customer_Event_Status" (name) VALUES ('SENT');
INSERT INTO "Customer_Event_Status" (name) VALUES ('ACCEPTED');
INSERT INTO "Customer_Event_Status" (name) VALUES ('DELETED');

INSERT INTO "Customer_Event_Priority" (name) VALUES ('HIGH');
INSERT INTO "Customer_Event_Priority" (name) VALUES ('AVERAGE');
INSERT INTO "Customer_Event_Priority" (name) VALUES ('LOW');
