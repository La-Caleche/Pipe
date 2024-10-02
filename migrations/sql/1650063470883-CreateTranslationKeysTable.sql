/* CreateTranslationKeysTable

You can write your SQL migration here.
Please be sure that your migration work before pushing them to git.
You can run your migration by using DatabaseMigration artifact and running
`java -jar DatabaseMigration.jar migrate`
*/

create table translation_keys
(
    id              int auto_increment,
    translation_key varchar(255)                        null,
    created_at      timestamp default current_timestamp null,
    updated_at      timestamp default current_timestamp null,
    constraint translation_keys_pk
        primary key (id)
);

create unique index translation_keys_translation_key_uindex
    on translation_keys (translation_key);
