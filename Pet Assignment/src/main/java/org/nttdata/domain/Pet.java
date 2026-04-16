package org.nttdata.domain;

public class Pet {
    private String name;
    private String owner;
    private String type;
    private String race;
    private Integer realAge;
    private Integer humanAge;

    public Pet(String name, String owner, String type, String race, Integer realAge, Integer humanAge) {
        this.name = name;
        this.owner = owner;
        this.type = type;
        this.race = race;
        this.realAge = realAge;
        this.humanAge = humanAge;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public Integer getRealAge() {
        return realAge;
    }

    public void setRealAge(Integer realAge) {
        this.realAge = realAge;
    }

    public Integer getHumanAge() {
        return humanAge;
    }

    public void setHumanAge(Integer humanAge) {
        this.humanAge = humanAge;
    }
}
