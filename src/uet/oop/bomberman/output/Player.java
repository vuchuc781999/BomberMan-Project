package uet.oop.bomberman.output;

public class Player {
    public String Name;
    public String points;
    public Player()
    {
        Name= "";
        points = "0";
    }
    public Player(String Name, String points)
    {
        this.Name = Name;
        this.points = points;
    }
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPoints() {
        return points;
    }
    public void setPoints(String points) {
        this.points = points;
    }
}
