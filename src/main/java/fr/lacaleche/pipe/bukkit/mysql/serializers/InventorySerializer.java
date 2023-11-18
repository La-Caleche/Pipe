package fr.lacaleche.pipe.bukkit.mysql.serializers;

import fr.lacaleche.core.databases.mysql.morph.serializer.interfaces.Serializer;
import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.BukkitPipe;
import fr.lacaleche.pipe.bukkit.mysql.serializers.PipeObjectInputStream;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class InventorySerializer implements Serializer<ItemStack[]> {

    public InventorySerializer() {}

    @Override
    public ByteArrayInputStream serialize(ItemStack[] items) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

        dataOutput.writeInt(items.length);

        for (ItemStack item : items) {
            dataOutput.writeObject(item);
        }

        dataOutput.close();
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    @Override
    public ItemStack[] deserialize(InputStream inputStream) throws IOException, ClassNotFoundException {
        if (inputStream == null || inputStream.available() == 0) return null;

        BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
        ItemStack[] items = new ItemStack[dataInput.readInt()];

        for (int i = 0; i < items.length; i++) {
            items[i] = (ItemStack) dataInput.readObject();
        }

        dataInput.close();
        return items;
    }

}
