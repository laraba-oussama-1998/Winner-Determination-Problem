import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class Main {
    private static int nbChances=4;

    public static int getNbChances() {
        return nbChances;
    }

    public static void setNbChances(int nbChances) {
        Main.nbChances = nbChances;
    }

    public static void main(String[] args) {
        PrintWriter output=null;
        try {
            int nbIterations;
            double moyTemps=0,moyQualite=0;

            for(int i=601;i<=610;i++) {
                System.out.println("*************** in" + i + " ****************");
                output = new PrintWriter(new BufferedWriter(new FileWriter("output" + i + ".csv")));

                Info_extracter f = new Info_extracter("instance/in" + i);
                output.print("in" + i + ",");
                double wp = 0.1;
                double best_gain = 0;




                /*
                for (int z = 0; z < 10; z++) {
                    nbIterations = 250;


                    long debut = System.nanoTime();
                    Solution s = f.RechercheLocaleStochastique(wp, nbIterations);
                    long fin = System.nanoTime();
                    moyQualite += s.getGain();
                    //System.out.println("*********  gain  "+s.getGain()+"   gain  ***********");
                    if (best_gain < s.getGain()) {
                        best_gain = s.getGain();
                    }
                    moyTemps += (fin - debut) / 1000000000.0;
                    System.out.println("********* best gain stochastic " + best_gain + " ***********");


                }*/








                    long debut = System.nanoTime();
                    Solution s = f.PSO();
                    long fin = System.nanoTime();
                    moyQualite += s.getGain();
                    //System.out.println("*********  gain  "+s.getGain()+"   gain  ***********");
                    if (best_gain < s.getGain()) {
                        best_gain = s.getGain();
                    }
                    moyTemps += (fin - debut) / 1000000000.0;









                System.out.println("*********" + moyTemps + "***********");

                moyTemps = 0 ;
            }

       /*
            nbIterations=250;
            for(int i=601;i<=610;i++) {

                System.out.println("*************** in"+i+" ****************");
                output=new PrintWriter(new BufferedWriter(new FileWriter("output"+i+".csv")));

                Info_extracter f=new Info_extracter("instance/in" +i);
                output.print("in"+i+",");

                int tenure = 50;
                int nbIterationBis = 6;
                double best_gain=0;

                    long debut = System.nanoTime();
                    Solution s = f.rechercheTaboue(nbIterations, tenure, nbIterationBis);
                    long fin = System.nanoTime();
                    moyQualite += s.getGain();

                    //System.out.println("*********  gain  "+s.getGain()+"   gain  ***********");
                    if(best_gain<s.getGain()){
                        best_gain=s.getGain();
                    }
                    System.out.println("********* best gain stochastic "+best_gain+" ***********");
                    moyTemps += (fin - debut) / 1000000000.0;
                    System.out.println("********* best gain stochastic "+moyTemps+" ***********");

                moyQualite = 0;
                moyTemps = 0;

            }
*/

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(output!=null){
                output.close();
            }
        }



    }
}