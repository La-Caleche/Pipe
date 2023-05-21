package fr.lacaleche.pipe.bukkit.modules.inventory.items;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.iface.ReadWriteItemNBT;
import fr.lacaleche.pipe.Pipe;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class ItemBuilder {

    private ItemStack item;
    private ItemMeta meta;

    public static final ItemBuilder EMPTY = new ItemBuilder(Material.AIR);
    public static final ItemBuilder BLACK_BACKGROUND = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).name("");
    public static final ItemBuilder DARK_GRAY_BACKGROUND = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).name("");
    public static final ItemBuilder GRAY_BACKGROUND = new ItemBuilder(Material.LIGHT_GRAY_STAINED_GLASS_PANE).name("");
    public static final ItemBuilder WHITE_BACKGROUND = new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).name("");


    public ItemBuilder(ItemStack item) {
        this.item = item;
        this.meta = item.getItemMeta();
    }

    public ItemBuilder(Material material) {
        this(new ItemStack(material));
    }

    public ItemStack getItem() {
        return this.item;
    }

    public ItemStack build() {
        this.applyItemMeta();
        return this.getItem();
    }

    public static ItemBuilder from(ItemStack item) {
        ItemBuilder builder = new ItemBuilder(item);
        return builder;
    }

    public ItemBuilder amount(int amount) {
        this.getItem().setAmount(amount);
        return this;
    }

    public ItemBuilder durability(int durability) {
        this.getItem().setDurability((short) durability);
        return this;
    }

    public ItemBuilder type(Material type) {
        this.applyItemMeta();
        this.getItem().setType(type);
        this.meta = this.getItem().getItemMeta();
        return this;
    }

    public ItemBuilder name(String name) {
        this.itemMeta().displayName(Component.text(name));
        return this;
    }

    public ItemBuilder name(Component component) {
        this.itemMeta().displayName(component.decoration(TextDecoration.ITALIC, false));
        return this;
    }

    public ItemBuilder lore(List<Component> component) {
        this.itemMeta().lore(component.stream().map(c -> c.decoration(TextDecoration.ITALIC, false)).toList());
        return this;
    }

    public ItemBuilder addLine(List<Component> component) {
        List<Component> lore = this.itemMeta().lore();
        if (lore == null) lore = new ArrayList<>();

        lore.addAll(component.stream().map(c -> c.decoration(TextDecoration.ITALIC, false)).toList());
        this.itemMeta().lore(lore);
        return this;
    }

    public ItemBuilder addLine(Component component) {
        List<Component> lore = this.itemMeta().lore();
        if (lore == null) lore = new ArrayList<>();

        lore.add(component.decoration(TextDecoration.ITALIC, false));
        this.itemMeta().lore(lore);
        return this;
    }

    public ItemBuilder enchantment(Enchantment enchantment, int level) {
        this.getItem().addEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder enchantment(Enchantment enchantment) {
        this.getItem().addUnsafeEnchantment(enchantment, 1);
        return this;
    }

    public ItemBuilder addPage(String... page) {
        this.bookMeta().addPages(Arrays.stream(page).map((s) -> Pipe.getBukkit().text().deserialize(s).decoration(TextDecoration.ITALIC, false)).toArray(Component[]::new));
        return this;
    }

    public ItemBuilder title(String title) {
        this.bookMeta().setTitle(title);
        return this;
    }

    public ItemBuilder author(String author) {
        this.bookMeta().setAuthor(author);
        return this;
    }

    public ItemBuilder setGlowing() {
        this.getItem().addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        this.itemMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    public ItemBuilder setUnbreakable() {
        this.itemMeta().setUnbreakable(true);
        this.itemMeta().addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        return this;
    }

    public ItemBuilder setPlayerHead(Player player) {
        this.nbt(nbt -> nbt.setString("SkullOwner", player.getName()));
        return this;
    }

    public ItemBuilder customModel(int model) {
        this.nbt(nbt -> nbt.setInteger("CustomModelData", model));
        return this;
    }

    public ItemBuilder setPlayerHeadTexture(String textureValue) {
        SkullMeta skullMeta = this.skullMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", textureValue));

        try {
            Method method = skullMeta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
            method.setAccessible(true);
            method.invoke(skullMeta, profile);
        } catch (Exception exception) {}

        return this;
    }

    public ItemMeta itemMeta() {
        return this.meta();
    }

    public BookMeta bookMeta() {
        return this.meta();
    }

    public SkullMeta skullMeta() {
        return this.meta();
    }

    private <T> T meta() {
        return (T) this.meta;
    }

    private void applyItemMeta() {
        this.getItem().setItemMeta(this.itemMeta());
    }

    public ItemBuilder nbt(Consumer<ReadWriteItemNBT> consumer) {
        this.applyItemMeta();

        NBTItem nbtItem = new NBTItem(this.getItem());
        consumer.accept(nbtItem);
        this.meta = nbtItem.getItem().getItemMeta();
        this.item.setItemMeta(this.itemMeta());

        return this;
    }

}
