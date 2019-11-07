import java.lang.Math; // headers MUST be above the first class
import java.util.ArrayList;

// one class needs to have a main() method
public class TuningCompare
{
  static ArrayList<IntervalReference> iref = new ArrayList<>();
  static int edo_compare = 16;
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
    iref.add(new IntervalReference("6ª Maior",16,15,9));
    iref.add(new IntervalReference("7ª menor",7,4,10));
    iref.add(new IntervalReference("7ª Maior",15,8,11));
    iref.add(new IntervalReference("8ª Justa",2,1,12));
    TuningType comparison = TuningType.EDO;
    switch (comparison) {
      case JUST:
        break;
      case EDO:
        EDOCompare edoc = new EDOCompare(edo_compare);
        edoc.compare(iref);
        break;
    }
  }
}

class EDOCompare implements IntervalCompare{
  double div;
  EDOCompare(double div){ this.div = div; }
  
  public void compare(ArrayList<IntervalReference> iref){
    //see Appendix1.tex for the math
    for (IntervalReference i : iref) {
      i.print();
      compareJust(i);
      compare12EDO(i);
      System.out.println("\n");
    }
  }
  
  void compareJust(IntervalReference i){
      double just = (double)i.just_n/i.just_d;
      double x = div*(Math.log(just) / Math.log(2));
      int just_r = (int)Math.round(x); //aproximating the just interval with an integer
      double just_r_edo = Math.pow(2,just_r/div); //using that approximation to calculate the interval
      double diff_in_cents = 1200*Math.log(just_r_edo/just)/ Math.log(2);//using 12-edo cents, of course
      System.out.println("    ------Comparing "+(int)div+"-EDO with the just interval------");
      System.out.println("    "+i.just_n+"/"+i.just_d+" = "+just);
      System.out.println("    exact = "+x); //that would give exact the just interval
      System.out.println("    round = "+just_r);
      System.out.println("    2^("+just_r+"/"+(int)div+") = "+just_r_edo);
      System.out.println("    ["+(int)div+"-edo/just] cents[1200] = "+diff_in_cents);
      System.out.println("    [12-edo/just] cents[1200] = "+i.just_to_12edo_diff_in_1200cents());
  }
  
  void compare12EDO(IntervalReference i){
    double edo12 = Math.pow(2,(double)i.edo12/12);
      double x = div*(Math.log(edo12) / Math.log(2));
      int edo_r = (int)Math.round(x);
      double edo_r_edo = Math.pow(2,edo_r/div); //using that approximation to calculate the interval
      double diff_in_cents = 1200*Math.log(edo_r_edo/edo12)/ Math.log(2);//using 12-edo cents
      System.out.println("    ------Comparing "+(int)div+"-EDO with 12-EDO------");
      System.out.println("    2^("+i.edo12+"/12) = "+edo12);
      System.out.println("    exact = "+x); //that would give exact the div-EDO interval
      System.out.println("    round "+edo_r);
      System.out.println("    2^("+edo_r+"/"+(int)div+") = "+edo_r_edo);
      System.out.println("    ["+(int)div+"edo/12-edo] cents[1200] = "+diff_in_cents);
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
    System.out.println("name: "+name+" ["+just_n+"/"+just_d+"] 12-edo["+edo12+"]");
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