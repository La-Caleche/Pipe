package fr.lacaleche.pipe.bukkit.persistentsblocks.interfaces;

import fr.lacaleche.pipe.bukkit.persistentsblocks.datatypes.*;
import fr.lacaleche.pipe.bukkit.persistentsblocks.interfaces.DataType;
import org.bukkit.persistence.PersistentDataType;

public interface DataTypes {

    DataType<Boolean> BOOLEAN = new BooleanDataType();
    DataType<Byte> BYTE = new ByteDataType();
    DataType<Short> SHORT = new ShortDataType();
    DataType<Integer> INTEGER = new IntegerDataType();
    DataType<Long> LONG = new LongDataType();
    DataType<Float> FLOAT = new FloatDataType();
    DataType<Double> DOUBLE = new DoubleDataType();
    DataType<String> STRING = new StringDataType();

}
