package fr.lacaleche.pipe.bukkit.modules.command;

import fr.lacaleche.core.utils.logger.Logger;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.ServerOperator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomPermissibleBase extends PermissibleBase {

    public CustomPermissibleBase(@Nullable ServerOperator opable) {
        super(opable);
    }

    @Override
    public boolean hasPermission(@NotNull String inName) {
        if (inName.equals("minecraft.admin.command_feedback")) return false;
        return super.hasPermission(inName);
    }

    @Override
    public boolean hasPermission(@NotNull Permission perm) {
        if (perm.getName().equals("minecraft.admin.command_feedback")) return false;
        return super.hasPermission(perm);
    }
}
