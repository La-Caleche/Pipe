package fr.lacaleche.pipe.bukkit.mysql.parsers;

import fr.lacaleche.core.databases.mysql.morph.builder.builders.InsertQueryBuilder;
import fr.lacaleche.core.databases.mysql.morph.builder.builders.UpdateQueryBuilder;
import fr.lacaleche.core.databases.mysql.morph.builder.sql.Insert;
import fr.lacaleche.core.databases.mysql.morph.builder.sql.Update;
import fr.lacaleche.core.databases.mysql.morph.interfaces.Query;
import fr.lacaleche.core.databases.mysql.morph.parser.parsers.AbstractBlobParser;
import fr.lacaleche.core.databases.mysql.morph.serializer.interfaces.Serializer;
import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.pipe.bukkit.mysql.annotations.BukkitBlob;
import org.joor.Reflect;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class BukkitBlobParser<T> extends AbstractBlobParser<T> {

    public BukkitBlobParser(Query<T> query, Object parent) {
        super(query, parent);
    }

    @Override
    public void apply(ResultSet rs, T t) throws SQLException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Reflect reflect = Reflect.on(t);

        for (Field f : this.getBukkitBlobs()) {
            BukkitBlob blob = f.getAnnotation(BukkitBlob.class);
            String columnName = blob.column();
            if (columnName.isBlank() || columnName.equals("."))
                columnName = f.getName();

            Serializer<T> serializer = this.instantiate(blob.serializer());
            InputStream stream = rs.getBlob(this.toSnakeCase(columnName)).getBinaryStream();

            try {
                this.setValue(reflect, f.getName(), serializer.deserialize(stream));
            } catch (IOException | ClassNotFoundException exception) {
                Logger.warn("Unable to deserialize blob %s for %s", columnName, t.getClass().getSimpleName());
            }
        }
    }

    @Override
    public void updateQuery(Reflect reflect, UpdateQueryBuilder<T> builder) {
        for (Field f : this.getBukkitBlobs()) {
            BukkitBlob blob = f.getAnnotation(BukkitBlob.class);
            String column = blob.column().isBlank() || blob.column().equalsIgnoreCase(".") ? f.getName() : blob.column();
            Serializer<T> serializer = this.instantiate(blob.serializer());

            try {
                ByteArrayInputStream inputStream = serializer.serialize(reflect.get(f.getName()));
                builder.set(new Update(this.toSnakeCase(column), inputStream));
            } catch (IOException exception) {
                Logger.warn("Unable to serialize blob " + column + " for " + this.getClazz().getSimpleName());
            }
        }
    }

    @Override
    public void insertQuery(Reflect reflect, InsertQueryBuilder<T> builder) {
        for (Field f : this.getBukkitBlobs()) {
            BukkitBlob blob = f.getAnnotation(BukkitBlob.class);
            String column = blob.column().isBlank() || blob.column().equalsIgnoreCase(".") ? f.getName() : blob.column();
            Serializer<T> serializer = this.instantiate(blob.serializer());

            try {
                ByteArrayInputStream inputStream = serializer.serialize(reflect.get(f.getName()));
                builder.insert(new Insert(this.toSnakeCase(column), inputStream));
            } catch (IOException exception) {
                Logger.warn("Unable to serialize blob " + column + " for " + this.getClazz().getSimpleName());
            }
        }
    }

    public List<Field> getBukkitBlobs() {
        return this.getAnnotatedFields(BukkitBlob.class);
    }

}
