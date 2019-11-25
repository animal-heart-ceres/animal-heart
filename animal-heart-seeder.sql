USE animal_heart_db;

INSERT INTO users (username, email, password, is_admin, is_organization) VALUE ('ryan', 'ryan@email.com', 'password', true, false);
INSERT INTO users (username, email, password, is_admin, is_organization) VALUE ('petsalive', 'petsalive@email.com', 'password', false, true);
INSERT INTO users (username, email, password, is_admin, is_organization) VALUE ('kyndall', 'kyndall@email.com', 'password', false, false);
INSERT INTO users (username, email, password, is_admin, is_organization) VALUE ('saveanimals', 'saveanimals@email.com', 'password', false, true);

INSERT INTO followers (follower_id, user_organization_id) VALUE (1, 2);
INSERT INTO followers (follower_id, user_organization_id) VALUE (1, 4);
INSERT INTO followers (follower_id, user_organization_id) VALUE (3, 2);
INSERT INTO followers (follower_id, user_organization_id) VALUE (3, 4);

INSERT INTO events (description, location, title, user_id) VALUE ('Doggo ipsum borking doggo heck fat boi, doggo. You are doing me the shock what a nice floof clouds shoober doing me a frighten, porgo many pats doggorino smol I am bekom fat, boof waggy wags maximum borkdrive. Doge I am bekom fat adorable doggo tungg, stop it fren. Ur givin me a spook such treat doggorino adorable doggo most angery pupper I have ever seen, tungg borking doggo thicc.', 'San Antonio, TX', 'Adoption Event', 2);
INSERT INTO events (description, location, title, user_id) VALUE ('Doggo ipsum borking doggo heck fat boi, doggo. You are doing me the shock what a nice floof clouds shoober doing me a frighten, porgo many pats doggorino smol I am bekom fat, boof waggy wags maximum borkdrive. Doge I am bekom fat adorable doggo tungg, stop it fren. Ur givin me a spook such treat doggorino adorable doggo most angery pupper I have ever seen, tungg borking doggo thicc.', 'Helotes, TX', 'Vaccine Event', 2);
INSERT INTO events (description, location, title, user_id) VALUE ('Doggo ipsum borking doggo heck fat boi, doggo. You are doing me the shock what a nice floof clouds shoober doing me a frighten, porgo many pats doggorino smol I am bekom fat, boof waggy wags maximum borkdrive. Doge I am bekom fat adorable doggo tungg, stop it fren. Ur givin me a spook such treat doggorino adorable doggo most angery pupper I have ever seen, tungg borking doggo thicc.', 'Leon Valley, TX', 'Adoption Event', 4);
INSERT INTO events (description, location, title, user_id) VALUE ('Doggo ipsum borking doggo heck fat boi, doggo. You are doing me the shock what a nice floof clouds shoober doing me a frighten, porgo many pats doggorino smol I am bekom fat, boof waggy wags maximum borkdrive. Doge I am bekom fat adorable doggo tungg, stop it fren. Ur givin me a spook such treat doggorino adorable doggo most angery pupper I have ever seen, tungg borking doggo thicc.', 'Kerrville, TX', 'PetsMart Adoption Event', 4);

INSERT INTO organization_profiles (description, name, tax_number, organization_id, address) VALUE ('Doggo ipsum borking doggo heck fat boi, doggo. You are doing me the shock what a nice floof clouds shoober doing me a frighten, porgo many pats doggorino smol I am bekom fat, boof waggy wags maximum borkdrive. Doge I am bekom fat adorable doggo tungg, stop it fren. Ur givin me a spook such treat doggorino adorable doggo most angery pupper I have ever seen, tungg borking doggo thicc.', 'Pets Alive', 123456789, 2, '1228 W Mariposa, San Antonio, TX 78201');
INSERT INTO organization_profiles (description, name, tax_number, organization_id, address) VALUE ('Doggo ipsum borking doggo heck fat boi, doggo. You are doing me the shock what a nice floof clouds shoober doing me a frighten, porgo many pats doggorino smol I am bekom fat, boof waggy wags maximum borkdrive. Doge I am bekom fat adorable doggo tungg, stop it fren. Ur givin me a spook such treat doggorino adorable doggo most angery pupper I have ever seen, tungg borking doggo thicc.', 'Save Animals', 123456788, 4, '16207 Canyon Shadow, San Antonio, TX 78232');

INSERT INTO user_profiles (address, first_name, last_name, user_id) VALUE ('10418 Shaenfield Rd, San Antonio, TX 78254', 'Ryan', 'Smith', 1);
INSERT INTO user_profiles (address, first_name, last_name, user_id) VALUE ('9807 Charline Lane, San Antonio, TX 28254', 'Kyndall', 'Simss', 3);

INSERT INTO animals (age, exotic_type, name, size, type, user_id) VALUE (5, 'Prairie Dog', 'Poppy', 'small', 'Exotic', 5);
INSERT INTO animals (age, name, size, type, user_id) VALUE (4, 'Ava', 'medium', 'Dog', 5);
INSERT INTO animals (age, name, size, type, user_id) VALUE (5, 'Poppy', 'large', 'Dog', 5);
INSERT INTO animals (age, name, size, type, user_id) VALUE (5, 'Michael', 'large', 'Cat', 5);
INSERT INTO animals (age, name, size, type, user_id) VALUE (5, 'Rodger', 'large', 'Cat', 5);
INSERT INTO animals (age, name, size, type, user_id) VALUE (5, 'Sally Mae', 'small', 'Dog', 5);

INSERT INTO comments (comment, animal_id, user_id) VALUE('Hey I can take that little guy.', 1, 1);
INSERT INTO comments (comment, animal_id, user_id) VALUE('Hey lets meet up so I can that little guy home', 1, 2);
INSERT INTO comments (comment, animal_id, user_id) VALUE('Hey lets meet up so I can that little guy home', 2, 3);
INSERT INTO comments (comment, animal_id, user_id) VALUE('Hey lets meet up so I can that little guy home', 2, 4);