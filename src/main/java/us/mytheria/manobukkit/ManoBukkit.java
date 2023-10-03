package us.mytheria.manobukkit;

import org.bukkit.Bukkit;
import us.mytheria.manobukkit.entities.ManoratorFactory;
import us.mytheria.manobukkit.entities.collider.ColliderFactory;

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
