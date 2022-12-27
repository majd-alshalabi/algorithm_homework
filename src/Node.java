public class Node {
    public double currentTime ;
    public double spentMoney ;
    public int health ;
    public Station station;

    public Node par ;

    public Node(double currentTime,double spentMoney,int health,Station station) {
        this.station = station ;
        this.currentTime = currentTime ;
        this.spentMoney = spentMoney ;
        this.health = health;
    }

    public Node deepCopy(){
        return new Node(currentTime , spentMoney , health , station.deepCopy());
    }

    public int heuristic(){
        return  0;
    }
}
