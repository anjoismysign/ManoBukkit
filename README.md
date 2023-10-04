# Mano'sBukkit
A small Bukkit oriented library. Features the following.

Decorators:
- ManoBlockState
- ManoEntity
- ManoWorld

As well as Colliders

Usage example:
````java
Entity stand = spawnEntity(location, EntityType.ARMOR_STAND);
stand.addPasenger(Bukkit.getPlayer("RikaRdzZ_");
ManoEntity manoStand = ManoratorFactory.getInstance().of(stand);
Location offset = location.clone();
offset.add(10, 0, 10);
manoStand.vehicleTeleport(offset); //teleports armorstand without unmounting passengers and passengers of passengers
````
