INSERT INTO public.roles (id, name) VALUES (0, 'ADMIN');
INSERT INTO public.roles (id, name) VALUES (1, 'USER');

INSERT INTO public.user_data (id, email, password, status, username) VALUES (0, 'admin', '$2a$12$9wmeOep1aBNwZ0RPMIcaK.fuaBg/gRLcsQ/0XqKCAazWkkrKmYRn2', 'ACTIVE', 'admin');

INSERT INTO public.user_data__roles (user_id, role_id) VALUES (0, 0);
INSERT INTO public.user_data__roles (user_id, role_id) VALUES (0, 1);
