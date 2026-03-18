package WagonFortShootout.java.framework.ai;

import WagonFortShootout.java.entity.Entity;

import java.util.HashSet;

public class Team {

    private static final HashSet<Team> ALL_TEAMS = new HashSet<Team>();

    public final byte ID;

    static {
        //Neutral
        new Team((byte)0);
        //Unafilliated
        new Team((byte)1);
    }

    private final HashSet<Entity> team_members = new HashSet<Entity>();

    public Team(byte id) {
        if(findTeam(id) == null) {
            ID = id;
            ALL_TEAMS.add(this);
        } else {
            if(id <= 1) {
                throw new IllegalArgumentException("Team ID 1 and 0 are reserved. 0 = Neutral, 1 = unaffiliated.");
            }
            throw new IllegalArgumentException("Team ID cannot be duplicate of existing team ID. Copy = " + id + ".");
        }
    }

    public void putEntity(Entity entity) {
        team_members.add(entity);
    }

    public void removeEntity(Entity entity) {
        team_members.remove(entity);
    }

    public HashSet<Entity> getEnemies() {
        //TODO make a team hive-mind so that instead of every ai entity calling hunt and seaching every square individually,
        // it is called once and each entity finds the one that is best for them
        if(ID == 0) {
            return new HashSet<Entity>();
        } else if(ID == 1) {
            return allEntities();
        } else {
            HashSet<Entity> enemies = new HashSet<Entity>();
            for(Team team: ALL_TEAMS) {
                if(this != team && team.ID != 0) {
                    enemies.addAll(team.team_members);
                }
            }
            return enemies;
        }
    }

    public boolean containsEntity(Entity entity) {
        return team_members.contains(entity);
    }

    public static Team findTeam(Entity entity) {
        for(Team team: ALL_TEAMS) {
            if(team.containsEntity(entity)) {
                return team;
            }
        }
        return null;
    }

    public static HashSet<Entity> allEntities() {
        HashSet<Entity> all = new HashSet<>();
        for(Team team: ALL_TEAMS) {
            all.addAll(team.team_members);
        }
        return all;
    }

    public static Team findTeam(byte id) {
        for(Team team: ALL_TEAMS) {
            if(team.ID == id) {
                return team;
            }
        }
        return null;
    }

    public static Team neutral() {
        return findTeam((byte)0);
    }

    public static Team unaffiliated() {
        return findTeam((byte)1);
    }
}
