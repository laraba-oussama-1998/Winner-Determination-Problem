import java.util.ArrayList;
import java.util.Iterator;

public class Solution {
    private ArrayList<Bid> bids;
    private ArrayList<Bid> conflict;
    private double gain;


    public Solution(){
        this.bids=new ArrayList<Bid>();
        this.gain=0;
        this.conflict=new ArrayList<Bid>();

    }

    public ArrayList<Bid> getBids() {
        return bids;
    }

    public void setBids(ArrayList<Bid> bids) {
        this.bids = bids;
    }

    public void addBid(Bid b){
        this.bids.add(b);
        this.gain+=b.getGain();
        this.concatConflict(b);
    }

    public void concatConflict(Bid b){
        Iterator<Bid> conflictIterator=b.getConflict().iterator();
        while(conflictIterator.hasNext()){
            Bid tmp=conflictIterator.next();
            if(!this.conflict.contains(tmp)){
                this.conflict.add(tmp);
            }
        }
    }

    public void forcedAddBid(Bid b){
        Iterator<Bid> bids=this.bids.iterator();
        ArrayList<Bid> toRemove=new ArrayList<Bid>();
        while(bids.hasNext()){
            Bid tmp=bids.next();
            //if(b==null)System.out.println("null b b b ");
            if(b.isInConflictWith(tmp)){
                toRemove.add(tmp);
                this.gain-=tmp.getGain();
            }
        }
        Iterator<Bid> removes=toRemove.iterator();
        while(removes.hasNext()){
            this.bids.remove(removes.next());
        }
        // set conflict pour m a j la liste de conflict (pour chaque bid de solution verifier tous les bids conlict
        // asq il exist dans la list des coflict sinon ajouter a l liste) () realise par methode concatConflict
        this.setConflict();

        this.bids.add(b);
        this.concatConflict(b);
        this.gain+=b.getGain();
    }

    public void forcedAddBid(Bid b, Info_extracter f){
        this.forcedAddBid(b);
        Iterator<Bid> bidsIterator=f.getBids().iterator();
        while(bidsIterator.hasNext()){
            Bid tmp=bidsIterator.next();
            if(!this.conflict.contains(tmp)){
                this.addBid(tmp);
            }
        }
    }

    public double getGain() {
        return gain;
    }

    public void setGain(double gain) {
        this.gain = gain;
    }

    public void setGain(){
        this.gain=0;
        Iterator<Bid> bids=this.bids.iterator();
        while(bids.hasNext()){
            this.gain+=bids.next().getGain();
        }
    }

    public Solution clone(){
        Solution solution=new Solution();
        Iterator<Bid> bids=this.bids.iterator();
        while(bids.hasNext()){
            solution.addBid(bids.next());
        }

        return solution;
    }

    public String toString(){
        String s="Gain de la solution = "+this.gain+"\n";
        s+="Enchères de la solutions : \n";
        Iterator<Bid> bids=this.bids.iterator();
        while(bids.hasNext()){
            s+=bids.next().toString();
        }
        return s;
    }



    public boolean equals(Solution s){
        if(s==null || s.getGain()!=this.gain || s.getBids().size()!=this.bids.size()){
            return false;
        }
        for(int i=0;i<this.bids.size();i++){
            boolean same=false;
            for(int j=0;j<s.getBids().size();j++){
                if(this.bids.get(i).equals(s.getBids().get(j))){
                    same=true;
                    break;
                }
            }
            if(!same){
                return false;
            }
        }
        return true;
    }

    public boolean equals(Object o){
        Solution s=(Solution) o;
        return this.equals(s);
    }

    public String getIds(){
        String s="";
        Iterator<Bid> bidsIterator=this.bids.iterator();
        while(bidsIterator.hasNext()){
            s+=bidsIterator.next().getId()+", ";
        }
        s+="\n";
        return s;
    }





    public ArrayList<Bid> getConflict() {
        return conflict;
    }

    public void setConflict(ArrayList<Bid> conflict) {
        this.conflict = conflict;
    }

    public void setConflict(){
        Iterator<Bid> bidsIterator=this.bids.iterator();
        while(bidsIterator.hasNext()){
            this.concatConflict(bidsIterator.next());
        }
    }
}