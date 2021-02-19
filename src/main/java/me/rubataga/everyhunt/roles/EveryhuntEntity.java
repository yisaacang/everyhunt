package me.rubataga.everyhunt.roles;

import org.bukkit.entity.Entity;

public class ManhuntEntity {

    private final Entity entity;

    public ManhuntEntity(Entity entity){
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

}