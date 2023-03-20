/* CascadeFeatureDelation

You can write your SQL migration here.
Please be sure that your migration work before pushing them to git.
You can run your migration by using DatabaseMigration artifact and running
`java -jar DatabaseMigration.jar migrate`
*/

dropOldFK {
    alter table feature_values
        drop foreign key feature_values_features_id_fk;
}

createNewFK {
    alter table feature_values
        add constraint feature_values_features_id_fk
            foreign key (feature_id) references features (id)
                on update cascade on delete cascade;
}

