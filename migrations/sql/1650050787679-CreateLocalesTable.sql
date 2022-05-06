/* CreateLocales

You can write your SQL migration here.
Please be sure that your migration work before pushing them to git.
You can run your migration by using DatabaseMigration artifact and running
`java -jar DatabaseMigration.jar migrate`
*/

createTable {
    create table locales
    (
        id         int auto_increment,
        slug       varchar(8)                          null,
        is_default boolean   default false             null,
        created_at timestamp default current_timestamp null,
        updated_at timestamp default current_timestamp null,
        constraint locales_pk
            primary key (id)
    );
}

createUniqueIndex {
    create unique index locales_slug_uindex
        on locales (slug);
    }
}