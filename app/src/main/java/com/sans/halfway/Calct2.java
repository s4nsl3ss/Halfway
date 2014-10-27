package com.sans.halfway;

import android.util.Log;

import java.util.ArrayList;


public class Calct2 {

    private ArrayList<JStep> asteps;
    private int totaldistance;

    public Calct2() {
    asteps=new ArrayList<JStep>();
    }

    public void add(JStep j){
        asteps.add(j);
    }

    public String toString(){
        String out="";
        for(JStep i: asteps){
        out+= i.toString()+"\n";
        }
    return out;
    }




    public JStep getHalf(){
        int out=0;
        ArrayList<JStep> ai= new ArrayList<JStep>();
        int i;
        for(i=0; i< asteps.size(); i++) {
            JStep jstep= asteps.get(i);
            int dis = jstep.getDistance();
            out += dis;
            if( out > (int) (totaldistance / 2.0)) {
               if (out -  (int) (totaldistance / 2.0) <  (int) (totaldistance / 2.0) - (out - dis)) {
                   ai.add(jstep);
               }
                out-=dis;
                break;
            }
            ai.add(jstep);
            }
        String s="";
        for(JStep j: ai){
            s+=j.toString() + "\n";
        }
             //Log.d("Calct2 getHalf: ", "half-distance: "+out + " totaldistance" + totaldistance);
        if(ai.size()>0)
         return ai.get(ai.size()-1);
       return null;
    }

    public GeoPoint getStart(){
        if(asteps == null)
        return null;
        return asteps.get(0).getStart();
    }


    public void setTotaldistance(int totaldistance) {
        this.totaldistance = totaldistance;
    }

}