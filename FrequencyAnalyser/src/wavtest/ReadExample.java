package wavtest;

import java.io.*;
import java.util.ArrayList;

public class ReadExample
{
	public static void main(String[] args)
	{
		BufferedWriter writer = null;
		try
		{
			File logFile = new File("Output_file.txt");
			writer = new BufferedWriter(new FileWriter(logFile));
            //writer.write("Hello world!");
			// Open the wav file specified as the first argument
			//String filename="C:\\Users\\Man Par\\workspace2\\wavtest\\src\\wavtest\\test.wav";
			String filename = "C:\\letsgomaboi\\wavtest\\wavtest\\src\\wavtest\\move1000a.wav";
			WavFile wavFile = WavFile.openWavFile(new File(filename));

			// Display information about the wav file
			wavFile.display();

			// Get the number of audio channels in the wav file
			int numChannels = wavFile.getNumChannels();

			// Create a buffer of 100 frames
			double[] buffer = new double[100 * numChannels];
			ArrayList<Double> Amp = new ArrayList<Double>();
			//required data
			int Tw = 5000;
			int T=50;
			double dt = 1/44100.0;   //
			int win_size = 1000; //
			double base_freq=1000.0;

			int framesRead;
			double min = Double.MAX_VALUE;
			double max = Double.MIN_VALUE;

			do
			{
				// Read frames into buffer
				framesRead = wavFile.readFrames(buffer, 100);

				// Loop through frames and look for minimum and maximum value
				for (int s=0 ; s<framesRead * numChannels ; s++)
				{
					System.out.println(buffer[s]);
					Amp.add(buffer[s]);
					if (buffer[s] > max) max = buffer[s];
					if (buffer[s] < min) min = buffer[s];
				}
			}
			while (framesRead != 0);

			// Close the wavFile
			wavFile.close();

			// Output the minimum and maximum value
			System.out.printf("Min: %f, Max: %f\n", min, max);
			
			ArrayList<Double> X = new ArrayList<Double>();
			ArrayList<Double> Freq = new ArrayList<Double>();
			
			for (int i=0; i<Tw; i++){
				if ((Amp.get(i)*Amp.get(i+1))<=0){
					if ((Amp.get(i+1)==0)&&((i+1)!=(i+win_size-1))){
						continue;
					}
					double dx = dt;
					double dy = (double)(Amp.get(i+1)-Amp.get(i));
					double m = (double)(dy/dx);
					double c = (double)(Amp.get(i)-(m*i*dt));
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
					
					if (Amp.get(endid)*Amp.get(oldend)<=0){
						if ((Amp.get(endid)==0)&&(endid!=(i+Tw-1))){
							continue;
						}
						double dx = dt;
						double dy = (double)(Amp.get(endid) - Amp.get(oldend));
						double m = (double)(dy/dx);
						double c = (double)(Amp.get(endid) - (m*endid*dt));
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
			
			for (int i=0; i<Avg_Freq.size(); i++){
				String op = String.valueOf(Avg_Freq.get(i))+"\n";
				writer.write(op);
			}
			System.out.println("Done\n");
		}
		catch (Exception e)
		{
			System.err.println(e);
		}
		try{
			writer.close();
		}
		catch (Exception e){
			System.out.println(e);
		}
	}
}
