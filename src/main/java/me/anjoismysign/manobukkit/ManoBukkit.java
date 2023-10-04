package me.anjoismysign.manobukkit;

import me.anjoismysign.manobukkit.entities.collider.ColliderFactory;
import me.anjoismysign.manobukkit.entities.decorators.implementations.ManoratorFactory;
import org.bukkit.Bukkit;

public class ManoBukkit {
    private static ManoBukkit instance;
    private final ColliderFactory colliderFactory;
    private final ManoratorFactory manoratorFactory;
    private final String craftBukkitPackage;
    private final String nmsPackage;

    private ManoBukkit() {
        colliderFactory = ColliderFactory.getInstance();
        manoratorFactory = ManoratorFactory.getInstance();
        craftBukkitPackage = Bukkit.getServer().getClass().getPackage().getName() + ".";
        nmsPackage = "net.minecraft.";
    }

    public static ManoBukkit getInstance() {
        if (instance == null) {
            instance = new ManoBukkit();
        }
        return instance;
    }

    public String getCraftBukkitClassPath(String clazz) {
        return craftBukkitPackage + clazz;
    }

    public String getNMSClassPath(String clazz) {
        return nmsPackage + clazz;
    }

    public Class<?> getCraftBukkitClass(String clazz) {
        try {
            return Class.forName(craftBukkitPackage + clazz);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Class<?> getNMSClass(String clazz) {
        try {
            return Class.forName(nmsPackage + clazz);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public ColliderFactory getColliderFactory() {
        return colliderFactory;
    }

    public ManoratorFactory getManoratorFactory() {
        return manoratorFactory;
    }
}
