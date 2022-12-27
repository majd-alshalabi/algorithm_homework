public class Road {
    public Double theSpeedOfTheTaxi ;
    public Double theSpeedOfTheBus ;
    final public double walkingSpeed = 5.5;

    final public double roadLength ;

    public Road(Double theSpeedOfTheTaxi, Double theSpeedOfTheBus , double roadLength) {
        this.theSpeedOfTheTaxi = theSpeedOfTheTaxi;
        this.theSpeedOfTheBus = theSpeedOfTheBus;
        this.roadLength = roadLength;
    }

    public Road deepCopy(){
        return new Road(theSpeedOfTheTaxi,theSpeedOfTheBus,roadLength);
    }
}
