package wavtest;

import java.util.ArrayList;

public class FrequencyAnalyzer {
	public static void main(String[] args){
		int Tw = 5000;
		int T=50;
		double dt = 1/1000.0;   //
		double Amp[] = new double [1000]; //
		int win_size = Tw; //
		double base_freq=1000.0;
		
		ArrayList<Double> X = new ArrayList<Double>();
		ArrayList<Double> Freq = new ArrayList<Double>();
		
		for (int i=0; i<Tw; i++){
			if ((Amp[i]*Amp[i+1])<=0){
				if ((Amp[i+1]==0)&&((i+1)!=(i+win_size-1))){
					continue;
				}
				double dx = dt;
				double dy = (double)(Amp[i+1]-Amp[i]);
				double m = (double)(dy/dx);
				double c = (double)(Amp[i]-(m*i*dt));
				double x = (double)((c/(m+0.00000000000001))*(-1.0));
				X.add(x);
			}
			double avg_delta_T = 0.0;
			for (int k=1; k<X.size(); k++){
				double deltaT = 2*((X.get(k))-(X.get(k-1)));
				avg_delta_T += deltaT;
			}
			
			if (X.size()<=1){
				continue;
			}
			avg_delta_T = (double)(avg_delta_T/((1.0)*(X.size()-1)));
			double data = 1.0/(avg_delta_T+0.000000000000001);
			Freq.add(data);
		}
		
		for (int i=Tw; i<(T-1); i++){
			if ((i+Tw)>T-1){
				break;
			}
			else {
				int startid = i;
				int endid = i+Tw;
				int oldend = endid - 1;
				double starttime = i*dt;
				if (X.size()>0){
					if (X.get(0)<starttime){
						X.remove(0);
					}
				}
				
				if (Amp[endid]*Amp[oldend]<=0){
					if ((Amp[endid]==0)&&(endid!=(i+Tw-1))){
						continue;
					}
					double dx = dt;
					double dy = (double)(Amp[endid] - Amp[oldend]);
					double m = (double)(dy/dx);
					double c = (double)(Amp[endid] - (m*endid*dt));
					double x = (double)((c/(m+0.00000000000001))*(-1.0));
					X.add(x);
				}
				
				double avg_delta_T = 0.0;
				for (int k=1; k<X.size(); k++){
					double deltaT = 2*((X.get(k))-(X.get(k-1)));
					avg_delta_T += deltaT;
				}
				
				if (X.size()<=1){
					continue;
				}
				avg_delta_T = (double)(avg_delta_T/((1.0)*(X.size()-1)));
				double data = 1.0/(avg_delta_T+0.000000000000001);
				Freq.add(data);
			}
		}
		
		ArrayList<Double> Final = new ArrayList<Double>();
		double BASE = base_freq;	
		double f_low = BASE-10.0;//BASE - (double)((3/100.0)*(BASE));
		double f_high =  BASE+10.0;//BASE + (double)((3/100.0)*(BASE));
		
		for (int i=0; i<Freq.size(); i++){
			double f = Freq.get(i);
			if ((f<=f_high)&&(f>=f_low)){
				Final.add(f);
			}
		}

		ArrayList<Double> Avg_Freq = new ArrayList<Double>();		      
		int window_ = Final.size()/1000;
		int	id_ = 0; 
		
		while(id_<Final.size()){
			if ((id_+window_)>=Final.size()){
				break;
			}
			double avg = 0.0;
			for (int i=id_; i<id_+window_; i++){
				avg +=Final.get(i);
			}
			double data = avg/window_;
			Avg_Freq.add(data);
			id_ = id_ + window_;
		}
	}
}
