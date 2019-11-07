import java.lang.Math; // headers MUST be above the first class
import java.util.ArrayList;

// one class needs to have a main() method
public class TuningCompare
{
  static ArrayList<IntervalReference> iref = new ArrayList<>();
  static int edo_compare = 19;
  // arguments are passed using the text field below this editor
  public static void main(String[] args)
  {    
    iref.add(new IntervalReference("2ª menor",16,15,1)); // we could use 17/16 to be be closer to 12-EDO
    iref.add(new IntervalReference("2ª Maior",9,8,2)); //closest to 12-edo
    iref.add(new IntervalReference("3ª menor",7,6,3));
    iref.add(new IntervalReference("3ª Maior",5,4,4));
    iref.add(new IntervalReference("4ª Justa",4,3,5));
    iref.add(new IntervalReference("tritone",17,12,6)); //both 17th and 12th harmonics are close to 12-edo
    iref.add(new IntervalReference("5ª Justa",3,2,7));
    iref.add(new IntervalReference("6ª menor",8,5,8));
    iref.add(new IntervalReference("6ª Maior",5,3,9));
    iref.add(new IntervalReference("7ª menor",7,4,10));
    iref.add(new IntervalReference("7ª Maior",15,8,11));
    iref.add(new IntervalReference("8ª Justa",2,1,12));
    TuningType comparison = TuningType.EDO;
    switch (comparison) {
      case JUST:
        break;
      case EDO:
        EDOPrinter.printEDO(edo_compare);
        JustApproximatorUsingEDO edoc = new JustApproximatorUsingEDO(edo_compare);
        edoc.compare(iref);
        EDOApproximatorUsingJust.approximateEDOinJustIntervals(edo_compare,iref);
        break;
    }
  }
}

class JustApproximatorUsingEDO implements IntervalCompare{
  double div;
  JustApproximatorUsingEDO(double div){ this.div = div; }
  
  public void compare(ArrayList<IntervalReference> iref){
    System.out.println("===========Approximating just intervals with "+(int)div+"-EDO===========\n");
    //see Appendix[1] for the math
    for (IntervalReference i : iref) {
      i.print();
      compareJust(i);
      compare12EDO(i);
    }
  }
  
  void compareJust(IntervalReference i){
      double just = i.getJust();
      double exact = div*(Math.log(just) / Math.log(2)); //the exact numerator of the exponent
      int rounded_num = (int)Math.round(exact); //rounded numerator
      double rounded_interval = Math.pow(2,rounded_num/div); //using that approximation to calculate the interval
      double diff_in_cents = 1200*Math.log(rounded_interval/just)/ Math.log(2);//using 12-edo cents, of course
      System.out.println("    ------Comparing "+(int)div+"-EDO with the just interval------");
      System.out.println("    "+i.just_n+"/"+i.just_d+" = "+just);
      System.out.println("    exact = "+exact); //that would give exact the just interval
      System.out.println("    round = "+rounded_num);
      System.out.println("    2^("+rounded_num+"/"+(int)div+") = "+rounded_interval);
      System.out.println("    ["+(int)div+"-edo/just] cents[12edo] = "+diff_in_cents);
      System.out.println("    [12-edo/just] cents[12edo] = "+i.just_to_12edo_diff_in_1200cents());
      System.out.println("");
  }
  
  void compare12EDO(IntervalReference i){
      double edo12 = i.get12EDO();
      double exact = div*(Math.log(edo12) / Math.log(2)); //the exact numerator of the exponent
      int rounded_num = (int)Math.round(exact); //rounded numerator
      double rounded_interval = Math.pow(2,rounded_num/div); //using that approximation to calculate the interval
      double diff_in_cents = 1200*Math.log(rounded_interval/edo12)/ Math.log(2);//using 12-edo cents
      System.out.println("    ------Comparing "+(int)div+"-EDO with 12-EDO------");
      System.out.println("    2^("+i.edo12+"/12) = "+edo12);
      System.out.println("    exact = "+exact); //that would give exact the div-EDO interval
      System.out.println("    round "+rounded_num);
      System.out.println("    2^("+rounded_num+"/"+(int)div+") = "+rounded_interval);
      System.out.println("    ["+(int)div+"edo/12-edo] cents[12edo] = "+diff_in_cents);
    System.out.println("\n");
  }
}

interface IntervalCompare{
  void compare(ArrayList<IntervalReference> iref);
}


class IntervalReference
{
  String name;
  int just_n; //just interval nominator
  int just_d; //just interval denominator
  int edo12; //edo number of intervals
  
  IntervalReference(String name, int just_n, int just_d, int edo12){
    this.name = name;
    this.just_n = just_n;
    this.just_d = just_d;
    this.edo12 = edo12;
  }
  
  void print(){
    //System.out.println("name: "+name+"\n---numerator: "+just_n+"\n---denumerador: "+just_d+"\n---12-edo: "+edo12);
    System.out.println(toString());
  }
  
  public String toString(){
    return name+" ["+just_n+"/"+just_d+"] 12-edo["+edo12+"]";
  }
  
  double getJust(){
    return just_n/(double)just_d;
  }
  
  double get12EDO(){
    return Math.pow(2,(double)edo12/12);
  }
  
  double just_to_12edo_diff_in_1200cents(){
    return 1200*Math.log(get12EDO()/getJust())/ Math.log(2);
  }
}

enum TuningType
{
  JUST,
  EDO;
}

class EDOPrinter{
  static void printEDO(double div){
    for(int i=1;i<=div;++i){
      double x = Math.pow(2,i/div);
      System.out.println("2^("+i+"/"+(int)div+") = "+x);
    }
    System.out.println("\n");
  }
}

class EDOApproximatorUsingJust{
  static void approximateEDOinJustIntervals(double div, ArrayList<IntervalReference> iref){
    System.out.println("===========Approximating "+(int)div+"-EDO with just intervals===========\n");
    for(int i=1;i<=div;++i){
      //the interval
      double x = Math.pow(2,i/div);
      //lets find the closest to x in the reference
      IntervalReference closest = null;
      double minInterval = 666;
      for (IntervalReference interval : iref) {
        /* we'll measure the difference in 12-edo cents because using simple division might be trickier.
           For example 1.5/1.2 and 1.2/1.5 are similarly distant in terms of pitch interval
           but the first is 1.25 and the latter is 0.8. So you by using cents you can just take it's 
           absolute value for comparison. Ex: -5 cents is equally distant to 5 cents*/
        double cents = 1200*Math.log(x/interval.getJust())/Math.log(2);
               cents = Math.abs(cents);
        if(cents<minInterval || closest == null){
          closest = interval;
          minInterval = cents;
        }
      }
      System.out.println("2^("+i+"/"+(int)div+") = "+x);
      System.out.println("    closest = "+closest.toString());
      double diffIn12EDOcents = 1200*Math.log(x/closest.getJust())/Math.log(2);
      System.out.println("    ["+(int)div+"-edo/just] in cents[12edo] = "+diffIn12EDOcents);
      System.out.println("");
    }
  }
}

//Appendix[1]
//\mbox{\textit{i} is a given interval which might be just or 12-edo} \\
//\mbox{ \textit{div} is the edo we are analyzing } \\
//\mbox{we can find the best natural number x that aproximates i} \\
//2^{\frac{x}{div}} = i \\
//log_2{(i)}=\frac{x}{div} \\
//div \cdot log_2{(i)}=x \\