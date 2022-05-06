/* CreateTranslationsTable

You can write your SQL migration here.
Please be sure that your migration work before pushing them to git.
You can run your migration by using DatabaseMigration artifact and running
`java -jar DatabaseMigration.jar migrate`
*/

createTable {
    create table translations
    (
        id              int auto_increment,
        locale          int                                 null,
        translation_key int                                 null,
        translation     text                                null,
        created_at      timestamp default current_timestamp null,
        updated_at      timestamp default current_timestamp null,
        constraint translations_pk
            primary key (id),
        constraint translations_locales_id_fk
            foreign key (locale) references locales (id)
                on update cascade on delete cascade,
        constraint translations_translation_keys_id_fk
            foreign key (translation_key) references translation_keys (id)
                on update cascade on delete cascade
    );
}

createUniqueIndex {
    create unique index translations_locale_translation_key_uindex
        on translations (locale, translation_key);
}


