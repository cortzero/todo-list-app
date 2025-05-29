CREATE TABLE IF NOT EXISTS users (
    id bigint NOT NULL,
    first_name character varying(20) NOT NULL,
    last_name character varying(20) NOT NULL,
    username character varying(20) NOT NULL,
    email character varying(30) NOT NULL,
    password character varying(255) NOT NULL,
    CONSTRAINT users_pkey PRIMARY KEY (id),
    CONSTRAINT users_email_key UNIQUE (email),
    CONSTRAINT users_username_key UNIQUE (username)
);

CREATE TABLE IF NOT EXISTS to_dos (
    id bigint NOT NULL,
    task text NOT NULL,
    complete boolean NOT NULL,
    user_id bigint NOT NULL,
    CONSTRAINT to_dos_pkey PRIMARY KEY (id),
    CONSTRAINT fk5ql6d2wedqbh7w84ck7ywr98j FOREIGN KEY (user_id) REFERENCES users (id)
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
);