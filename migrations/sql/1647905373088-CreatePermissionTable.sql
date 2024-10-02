/* CreatePermissionTable

You can write your SQL migration here.
Please be sure that your migration work before pushing them to git.
You can run your migration by using DatabaseMigration artifact and running
`java -jar DatabaseMigration.jar migrate`
*/

create table permissions
(
    id         int auto_increment,
    slug       varchar(255)                        null,
    created_at timestamp default current_timestamp null,
    updated_at timestamp default current_timestamp null,
    constraint permissions_pk
        primary key (id)
);

create unique index permissions_slug_uindex
    on permissions (slug);
