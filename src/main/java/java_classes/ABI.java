package java_classes;


import java.util.ArrayList;

public class ABI {
    public String name;
    public Action action;
    public Status status;
    public Measurement var1;//type of Measurement
    public int amount;//Measurement quantity
    public Comparator comp;//Comparator
    public int var2;
    public boolean temp;
    public ArrayList<String> seqReqs;
    public boolean SR1;
    public boolean SR2;
    public boolean SR3;
    public boolean SR4;
    public boolean SR5;

//Constructor with relevant parameters
    public ABI(Action action, Status status, Measurement var1, int amount, Comparator comp, int var2, boolean temp) {
        super();
        this.name= "no name";
        this.status = status;
        this.action = action;
        this.var1 = var1;
        this.amount = amount;
        this.comp = comp;
        this.var2 = var2;
        this.temp = temp;
        this.seqReqs = new ArrayList<>();
        this.SR1 = false;
        this.SR2 = false;
        this.SR3 = false;
        this.SR4 = false;
        this.SR5 = false;
    }

    //Empty Constructor
    public ABI() {
        this.name="unnamed";
        this.status = status.na;
        this.action = action.alert;
        this.var1 = Measurement.NA;
        this.amount = -1;
        this.comp = Comparator.na;
        this.var2 = -1;
        this.temp = false;
        this.seqReqs =new ArrayList<String>();
        this.SR1 = false;
        this.SR2 = false;
        this.SR3 = false;
        this.SR4=false;
        this.SR5 = false;
    }

    //Print method used for testing
    public String getString(){
        String returnString = "Name: "+this.name+"\nStatus: "+this.status+ "\nAction: " + this.action+"\nMeasurement: "
        +this.var1+"\n amount: "+ this.amount + "\nComparator: "+this.comp;

        return returnString;
    }
}

