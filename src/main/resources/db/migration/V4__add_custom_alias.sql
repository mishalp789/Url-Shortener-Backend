ALTER TABLE urls
    ADD COLUMN custom_alias VARCHAR(100);

ALTER TABLE urls
    ADD CONSTRAINT uk_custom_alias
        UNIQUE(custom_alias);