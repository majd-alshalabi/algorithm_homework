import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Station {
    public final int id;
    public Integer busWaitTime;
    public Integer taxiWaitTime;
    public final List<Transportation> transportation;

    public final List<Pair<Station,Road>> nextStations = new ArrayList<>();

    public Station(int id, Integer busWaitTime, Integer taxiWaitTime, List<Transportation> transportation) {
        this.id = id;
        this.busWaitTime = busWaitTime;
        this.taxiWaitTime = taxiWaitTime;
        this.transportation = transportation;
    }

    public Station deepCopy(){
        Station temp = new Station(id,busWaitTime,taxiWaitTime,new ArrayList<>(transportation));
        temp.nextStations.addAll(nextStations) ;
        return temp;
    }

}


abstract class Transportation{
    final public int cost ;
    final public int healthCost;

    public Transportation(int cost , int healthCost) {
        this.cost = cost;
        this.healthCost = healthCost;
    }
}

class Taxi extends Transportation {
    public Taxi(int cost) {
        super(cost,5);
    }
}

class Bus extends Transportation {
    public Bus(int cost) {
        super(cost,5);
    }
}

class OnFeet extends Transportation {
    public OnFeet() {
        super(0,10);
    }
}
