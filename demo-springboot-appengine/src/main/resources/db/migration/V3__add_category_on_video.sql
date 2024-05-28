ALTER TABLE videos
    ADD COLUMN category_id integer NOT NULL;
ALTER TABLE videos
    ADD CONSTRAINT fk_video_categoria FOREIGN KEY (category_id) REFERENCES categories (id);