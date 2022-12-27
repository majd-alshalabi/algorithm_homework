import java.io.Console;
import java.util.*;

public class Main {
    public static List<Station> stations = new ArrayList<>();
    public static HashMap<Integer, Boolean>vis = new HashMap<>();
    static int money ;

    static Station  theEndStation ;
    public static void AStar(Node node ){

        PriorityQueue<Node> queue = new PriorityQueue<Node>(new NodeComparator());
        queue.add(node);
        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();

            vis.put(currentNode.station.id , true) ;

            if(currentNode.station.id == theEndStation.id)
            {
                List<Station> stations1 = new ArrayList<>();
                stations1.add(currentNode.station.deepCopy());

                while (currentNode.par != null)
                {
                    currentNode = currentNode.par;
                    stations1.add(currentNode.station.deepCopy());
                }
                Collections.reverse(stations1);
                stations1.forEach(station -> {
                    System.out.println(station.id);
                });
                return;
            }

            for (Pair<Station, Road> stationRoadPair : currentNode.station.nextStations) {
                if (!vis.containsKey(stationRoadPair.getKey().id)) {
                    for (Transportation transportation:currentNode.station.transportation) {
                        if(transportation instanceof Taxi)
                        {
                            double nextNodeTime = currentNode.currentTime + (stationRoadPair.getValue().roadLength / stationRoadPair.getValue().theSpeedOfTheTaxi) + currentNode.station.taxiWaitTime;
                            double nextNodeSpentMoney = currentNode.spentMoney + ( transportation.cost * stationRoadPair.getValue().roadLength);
                            int currentHealth = currentNode.health + transportation.healthCost;
                            Node nextNode = new Node(nextNodeTime,nextNodeSpentMoney,currentHealth,stationRoadPair.getKey().deepCopy());
                            nextNode.par = currentNode;
                            if(nextNodeSpentMoney <= money && currentHealth < 100)
                                queue.add(nextNode);
                        }
                        else if(transportation instanceof Bus)
                        {
                            double nextNodeTime = currentNode.currentTime + (stationRoadPair.getValue().roadLength / stationRoadPair.getValue().theSpeedOfTheBus) + currentNode.station.busWaitTime;
                            double nextNodeSpentMoney = currentNode.spentMoney + ( transportation.cost );
                            int currentHealth = currentNode.health + transportation.healthCost;
                            Node nextNode = new Node(nextNodeTime,nextNodeSpentMoney,currentHealth,stationRoadPair.getKey().deepCopy());
                            nextNode.par = currentNode;

                            if(nextNodeSpentMoney <= money && currentHealth < 100)
                            {
                                queue.add(nextNode);
                            }
                        }
                        else if(transportation instanceof OnFeet)
                        {
                            double nextNodeTime = currentNode.currentTime + (stationRoadPair.getValue().roadLength / stationRoadPair.getValue().walkingSpeed);
                            double nextNodeSpentMoney = currentNode.spentMoney ;
                            int currentHealth = currentNode.health + transportation.healthCost;
                            Node nextNode = new Node(nextNodeTime,nextNodeSpentMoney,currentHealth,stationRoadPair.getKey().deepCopy());
                            nextNode.par = currentNode;

                            if(nextNodeSpentMoney <= money && currentHealth < 100)
                                queue.add(nextNode);
                        }
                    }
                }
            }
        }
    }
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        //System.out.print("Enter your current money : ");
        money = scan.nextInt();
        //System.out.print("Enter the number of station : ");
        int numOfStation = scan.nextInt();
        for (int i = 0; i < numOfStation ; i++) {
            //System.out.println("Station : " + ( i + 1 ));
            //System.out.print("does taxi or bus cross from here : ");
            boolean isTaxiOrBus = scan.nextBoolean();
            Station station = new Station(i + 1 , null , null , new ArrayList<>());
            if(isTaxiOrBus)
            {
                //System.out.print("how much does taxi cost : ");
                int taxiCost = scan.nextInt();
                //System.out.print("how much you will wait to get taxi : ");
                station.taxiWaitTime = scan.nextInt();
                Taxi taxi = new Taxi(taxiCost);
                //System.out.print("how much does bus cost : ");
                int busCoast = scan.nextInt();
                //System.out.print("how much you will wait to get bus : ");
                station.busWaitTime = scan.nextInt();
                Bus bus = new Bus(busCoast);
                station.transportation.add(bus);
                station.transportation.add(taxi);
            }
            station.transportation.add(new OnFeet());
            stations.add(station);
        }
        //System.out.print("Enter the number of roads : ");
        int roadNumber = scan.nextInt();
        for (int i = 0; i < roadNumber; i++) {
            //System.out.print("Enter the start station id : ");
            int startStationId = scan.nextInt();
            //System.out.print("Enter the end station id : ");
            int endStationId = scan.nextInt();
            //System.out.print("Enter the road length : ");
            int roadLength = scan.nextInt();
            Road road = new Road(null , null , roadLength);
            Station endStation = null ;
            Station startStation = null ;
            boolean checkIfTaxiOrBusCrossThisStation = false;
            for (Station tempStation:
                 stations) {
                if(tempStation.id == startStationId)
                {
                    if(tempStation.transportation.size() == 3)
                        checkIfTaxiOrBusCrossThisStation = true ;
                    startStation = tempStation ;
                }
                if(tempStation.id == endStationId)
                {
                    endStation = tempStation ;
                }
            }
            if(checkIfTaxiOrBusCrossThisStation)
            {
//                System.out.print("Enter the speed of the bus : ");
                double speedOfTheBus = scan.nextDouble();
//                System.out.print("Enter the speed of the taxi : ");
                double speedOfTheTaxi = scan.nextDouble();
                road.theSpeedOfTheBus = speedOfTheBus;
                road.theSpeedOfTheTaxi = speedOfTheTaxi;
            }
            if(endStation != null && startStation != null)
            {
                startStation.nextStations.add(new Pair<>(endStation , road.deepCopy()));
            }
        }

        //System.out.print("Enter start station id : ");
        int startStationId = scan.nextInt() ;
        //System.out.print("Enter end station id : ");
        int endStationId = scan.nextInt() ;

        Station startStation = null ;
        for (Station tempStation:
             stations) {
            if(tempStation.id == startStationId)
            {
                startStation = tempStation.deepCopy() ;
            }
            if(endStationId == tempStation.id)
            {
                theEndStation = tempStation.deepCopy() ;
            }
        }
        if(startStation != null && theEndStation != null)
        {
            AStar(new Node(0,0,0,startStation));
        }
    }
    static class  NodeComparator implements Comparator<Node> {
        public int compare(Node s1, Node s2) {
            if (s1.currentTime < s2.currentTime)
                return -1;
            else if (s1.currentTime > s2.currentTime)
                return 1;
            else {
                return Integer.compare(s2.health, s1.health);
            }
        }
    }
}


