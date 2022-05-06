/* CreateRanksTable

You can write your SQL migration here.
Please be sure that your migration work before pushing them to git.
You can run your migration by using DatabaseMigration artifact and running
`java -jar DatabaseMigration.jar migrate`
*/

createTable {
    create table ranks
    (
        id         int auto_increment,
        slug       varchar(255)                        null,
        color_code varchar(7)                          null,
        created_at timestamp default CURRENT_TIMESTAMP null,
        updated_at timestamp default CURRENT_TIMESTAMP null,
        constraint ranks_pk
            primary key (id)
    );
}

createUniqueIndex {
    create unique index ranks_slug_uindex
        on ranks (slug);
}
