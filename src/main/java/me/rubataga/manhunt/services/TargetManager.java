package me.rubataga.manhunt.services;

import me.rubataga.manhunt.roles.Hunter;
import me.rubataga.manhunt.roles.RoleEnum;
import me.rubataga.manhunt.roles.Target;

import org.bukkit.entity.Entity;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TargetManager {

    private static final Map<Entity, Hunter> hunters = new HashMap<>();
    private static final Map<Entity, Target> targets = new HashMap<>();
    private static final Map<Entity, Target> runners = new HashMap<>();
    private static final List<Target> runnerList = new LinkedList<>();

    public static boolean hasRole(Entity entity, RoleEnum role){
        if(role.equals(RoleEnum.HUNTER)){
            return hunters.containsKey(entity);
        } else if (role.equals(RoleEnum.TARGET)){
            return targets.containsKey(entity);
        } else if (role.equals(RoleEnum.RUNNER)){
            return runners.containsKey(entity);
        }
        return false;
    }

    public static void addHunter(Hunter hunter){
        hunters.put(hunter.getEntity(), hunter);
    }

    public static void removeHunter(Entity hunter){
        hunters.remove(hunter);
    }

    public static void addTarget(Target target){
        targets.put(target.getEntity(), target);
    }

    public static void removeTarget(Entity target){
        targets.remove(target);
    }

    public static void addRunner(Target runner){
        runners.put(runner.getEntity(), runner);
        runnerList.add(runner);
    }

    public static void removeRunner(Entity runner){
        runners.remove(runner);
        runnerList.remove(runners.get(runner));
    }

    public static Map<Entity, Hunter> getHunters() {
        return hunters;
    }

    public static Map<Entity, Target> getTargets() {
        return targets;
    }

    public static Map<Entity, Target> getRunners() {
        return runners;
    }

    public static List<Target> getRunnerList() {
        return runnerList;
    }

}
