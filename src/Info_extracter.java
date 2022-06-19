import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Info_extracter {
    private ArrayList<Bid> bids;
    private short nombreBids;
    private short nombreobjet;

    public Info_extracter(){
        this.bids=new ArrayList<Bid>();
        this.nombreBids=0;
        this.nombreobjet=0;
    }

    public Info_extracter(String nomFichier){
        this();
        System.out.println("Début de la création de la formule WDP");
        try {
            BufferedReader br=new BufferedReader(new FileReader(nomFichier));
            String ligne=br.readLine();
            String[] mots=ligne.split(" ");
            this.nombreobjet=Short.parseShort(mots[0]);
            this.nombreBids=Short.parseShort(mots[1]);
            ligne=br.readLine();
            while(ligne!=null){
                mots=ligne.split(" ");
                Bid bid=new Bid();
                bid.setGain(Double.parseDouble(mots[0]));
                for(int i=1;i<mots.length;i++){
                    bid.addLot(Short.parseShort(mots[i]));
                }
                bid.checkAddToConflict(this.bids);
                this.bids.add(bid);
                ligne=br.readLine();
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Fin de la création de la formule WDP");
    }


    public String toString(){
        String s="Nombre de objet : "+this.nombreobjet+"\n";
        s+="Nombre des offres : "+this.nombreBids+"\n";
        Iterator<Bid> bids=this.bids.iterator();
        while(bids.hasNext()){
            s+=bids.next().toString();
        }
        s+="\n";
        return s;
    }

    //Random key encoding algorithme modifié
    public Solution genererRandom(){
        Solution s=new Solution();

        //Génération du vecteur r
        ArrayList<Integer> r=new ArrayList<Integer>();
        for(int i=0;i<this.nombreBids;i++){
            int tmp=(int)(Math.random()*10000)%this.nombreobjet;
            // System.out.println(tmp);
            r.add(tmp);
        }

        int i=this.nombreBids-1;
        while(i>=0){
            int index=r.indexOf(i);
            while(index==-1){
                i--;
                index=r.indexOf(i);
            }
            Bid tmp=this.bids.get(index);
            r.set(index,-1);
            if(!tmp.isInConflict(s.getBids())){
                s.addBid(tmp);
            }
        }

        return s;
    }

    public Bid bestBid(Solution s){
        Iterator<Bid> bids=this.bids.iterator();
        Bid bestBid= null;
        double bestGainBid=-10000;
        while(bids.hasNext()){
            Bid tmp=bids.next();
            if(!s.getBids().contains(tmp)){
                Iterator<Bid> conflicts=tmp.getConflict().iterator();
                double perte=0;
                while(conflicts.hasNext()){
                    Bid tmpConflinct=conflicts.next();
                    if(s.getBids().contains(tmpConflinct)){
                        perte+=tmpConflinct.getGain();
                    }
                }
                double gainBid=tmp.getGain()-perte;
                if(gainBid>bestGainBid){
                    bestGainBid=gainBid;
                    bestBid=tmp.clone();
                }
            }
        }
        return bestBid;
    }

    
/*
    public Solution RechercheLocaleStochastique(double wp,int nbIterationsMax){
        Solution s=genererRandom(),best=s.clone();
        for(int nbIteration=0;nbIteration<nbIterationsMax;nbIteration++){
            Bid bid=null;
            double r=Math.random();
            if(r<wp){
                bid=this.bids.get((int)(Math.random()*10000)%this.nombreBids);
            }
            else{
                bid=bestBid(s);
            }
            if(bid!=null){
                s.forcedAddBid(bid,this);
            }
            if(s.getGain()>best.getGain()){
                best=s.clone();
            }
        }
        return best;
    }

*/





    public Solution PSO(){




        double w=0.2,r1=0,r2=0;
        double c1=1.8,c2=1.9;
        ArrayList<Solution> population_solution = new  ArrayList();
        ArrayList<Integer> volacity = new  ArrayList();
        ArrayList<Solution> pbest = new  ArrayList();
        Solution gbest = null;
        Solution s;
        int best_solution_pos=0;
        double best_gain=0;
        int volaci=0;
        for(int i=0; i<100;i++) {

            do {
                s = genererRandom();

            }while (population_solution.contains(s));

            do{
                volaci= (int) (Math.random()*100)%s.getBids().size();
               // System.out.println(volaci);
            }while(volaci<1);

            if(s.getGain()>best_gain){
                best_solution_pos=i;
                best_gain = s.getGain();
                gbest = s.clone();

            }
            population_solution.add(s);
            volacity.add(volaci);
            pbest.add(s);


        }

        System.out.println("meilleure solution de la population initiale est : "+best_gain+"" +
                " dans la position "+best_solution_pos);

        for(int i=0;i<20;i++){
            position_movement( population_solution, volacity, pbest, gbest, r1 ,r2, c1, c2,w);

            System.out.println("mouvment number : "+i);

            for(Solution pbest_iterator :pbest){
                if(gbest.getGain()<pbest_iterator.getGain()){

                    gbest=pbest_iterator.clone();
                }
            }
            System.out.println("iteration  "+i+" " +
                    " best bid  "+gbest.getGain());
           // System.out.println("meilleure solution de la population initiale est : "+gbest.getBids());

        }

        return gbest ;


    }








    public void position_movement(ArrayList<Solution> sol,ArrayList<Integer> volacity,
                                  ArrayList<Solution> pbest,Solution gbest,
                                  double r1 ,double r2,double c1, double c2,double w) {

        boolean mardtni=true;
        for (int i = 0; i < sol.size(); i++) {
            Solution pos = new Solution();
           // System.out.println("i = "+i);
            do {
                r1 = Math.random();
                r2 = Math.random();

                volacity.set(i, ((int) (w * volacity.get(i)
                        + c1 * r1 * (pbest.get(i).getGain() - sol.get(i).getGain())
                        + c2 * r2 * (gbest.getGain() - sol.get(i).getGain()))) % sol.get(i).getBids().size());


                if (gbest.getGain() - sol.get(i).getGain() < 0)
                    mardtni = false;

                //System.out.println("volacity "+volacity.get(i));
                //System.out.println("bids "+sol.get(i).getBids().size());
            } while (volacity.get(i) < 0);


            ArrayList<Integer> position_move = new ArrayList<>(volacity.get(i));
            int pose;

            for (int j = 0; j < volacity.get(i); j++) {
                do {

                    do {
                        pose = ((int) (Math.random() * 100))%sol.get(i).getBids().size();

                    } while (pose < 0);

                } while (position_move.contains(pose));

                position_move.add(pose);
            }


            pos = (Solution) sol.get(i).clone();
            ArrayList listBids = new ArrayList();
            for (Integer ps : position_move) {
                Bid bid;
                do {
                    bid = bids.get((int) ((Math.random() * 10000000) % bids.size()));
                } while (!pos.getBids().contains(bid));

                pos.forcedAddBid(bid,this);
            }
            //pos.setConflict();

           // sol.fo(bestBid(pos));
            if(bestBid(pos)!=null)
            sol.get(i).forcedAddBid(bestBid(pos),this);

        }

        for (int i = 0; i < sol.size(); i++) {
            if (sol.get(i).getGain() >= pbest.get(i).getGain()) {
                pbest.set(i, sol.get(i).clone());


            }


        }


    }

/*
    public Solution rechercheTaboue(int nbIterationsMax,int tenure,int iterDiversification){
        Solution s=genererRandom(),best=s.clone();
        ArrayList<Bid> listeTaboue=new ArrayList<Bid>();
        int indiceTaboue=0,iterationSansAmelioration=0;
        for(int nbIterations=0;nbIterations<nbIterationsMax;nbIterations++){
            Bid bid=bestBid(s);
            if(bid!=null && !listeTaboue.contains(bid)){
                //Gestion de tenure liste taboue
                if(listeTaboue.size()>=tenure){
                    listeTaboue.remove(indiceTaboue);
                }

                s.forcedAddBid(bid,this);

                listeTaboue.add(indiceTaboue,bid);
                indiceTaboue=(indiceTaboue+1)%tenure;

                if(s.getGain()>best.getGain()){
                    best=s.clone();
                    iterationSansAmelioration=0;
                }
                else{

                        //Diversification
                        s=genererRandom();

                }
            }
            else{
                    //Diversification
                    s=genererRandom();


            }

        }

        return best;
    }
    */




    public ArrayList<Bid> getBids() {
        return bids;
    }

    public void setBids(ArrayList<Bid> bids) {
        this.bids = bids;
    }

    public short getNombreBids() {
        return nombreBids;
    }

    public void setNombreBids(short nbBids) {
        this.nombreBids = nbBids;
    }

    public short getnombreobjet() {
        return nombreobjet;
    }

    public void setNombreobjet(short nbLots) {
        this.nombreobjet = nbLots;
    }


}
