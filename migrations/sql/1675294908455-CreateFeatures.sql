/* CreateFeatures

You can write your SQL migration here.
Please be sure that your migration work before pushing them to git.
You can run your migration by using DatabaseMigration artifact and running
`java -jar DatabaseMigration.jar migrate`
*/

createFeaturesTable {
    create table features
    (
        id          int auto_increment,
        name        varchar(255)                        not null,
        created_at  timestamp default current_timestamp not null,
        updated_at  timestamp default current_timestamp not null,
        constraint features_pk
            primary key (id)
    );
}

createFeatureValuesTable {
    create table feature_values
    (
        id            int auto_increment,
        feature_id    int                                 not null,
        feature_value text                                not null,
        created_at    timestamp default current_timestamp not null,
        updated_at    timestamp default current_timestamp null,
        constraint feature_values_pk
            primary key (id),
        constraint feature_values_pk2
            unique (feature_id),
        constraint feature_values_features_id_fk
            foreign key (feature_id) references features (id)
    );
}
