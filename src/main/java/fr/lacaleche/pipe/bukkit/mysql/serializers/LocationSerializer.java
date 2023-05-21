package fr.lacaleche.pipe.bukkit.mysql.serializers;

import fr.lacaleche.core.databases.mysql.morph.serializer.interfaces.Serializer;
import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.BukkitPipe;
import fr.lacaleche.pipe.bukkit.modules.chat.renderers.PipeViewerUnaware;
import net.minecraft.server.packs.linkfs.LinkFileSystem;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

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
        BukkitPipe pipe = Pipe.getBukkit();

        PipeObjectInputStream dataInput = new PipeObjectInputStream(inputStream);
        Map<String, Object> args = dataInput.resolveWrapper();

        World world = null;
        if (args.containsKey("world") && args.get("world") != null) world = pipe.getPlugin().getServer().getWorld((String) args.get("world"));
        return (T) new Location(world, NumberConversions.toDouble(args.get("x")), NumberConversions.toDouble(args.get("y")), NumberConversions.toDouble(args.get("z")), NumberConversions.toFloat(args.get("yaw")), NumberConversions.toFloat(args.get("pitch")));
    }

}
