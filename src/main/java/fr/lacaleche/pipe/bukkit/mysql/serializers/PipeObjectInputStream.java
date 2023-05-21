package fr.lacaleche.pipe.bukkit.mysql.serializers;

import fr.lacaleche.core.utils.logger.Logger;
import org.joor.Reflect;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Map;

final class PipeObjectInputStream extends ObjectInputStream {

    public PipeObjectInputStream(InputStream in) throws IOException {
        super(in);
    }

    public Map<String, Object> resolveWrapper() throws IOException, ClassNotFoundException {
        Object wrapper = super.readObject();
        if (wrapper != null && Class.forName("org.bukkit.util.io.Wrapper").isInstance(wrapper)) {
            return Reflect.on(wrapper).get("map");
        }

        return Map.of();
    }

}
