package fr.lacaleche.pipe.bukkit.mysql.serializers;

import fr.lacaleche.core.databases.mysql.morph.serializer.interfaces.Serializer;
import org.bukkit.Location;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class LocationSerializer<T extends Location> implements Serializer<T> {

    public LocationSerializer() {}

    @Override
    public ByteArrayInputStream serialize(T t) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
        dataOutput.writeObject(t);
        dataOutput.close();
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    @Override
    public T deserialize(InputStream inputStream) throws IOException, ClassNotFoundException {
        if (inputStream == null || inputStream.available() == 0) return null;
        BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
        T object = (T) dataInput.readObject();
        dataInput.close();
        return object;
    }

}
