Wave-Frequency-Analyser-JavaProject
===================================
This project can be used to read in a .wav file and then use the amplitude vs time data to get the frequency vs time data.
The frequency vs time can then be used for many useful purposes, which are not served by only the amplitude vs time data.
For reading the .wav file a project called JAVA WAV FILE IO has been used 
http://www.labbookpages.co.uk/audio/javaWavFiles.html . 
Currently this project is buggy and doesn't work for samples recorded in noisy environment.
The main bug lies in the reader itself.

Future plans:-
----------------------
Try to improve the reader as currently it throws exceptions for samples recorded in noisy environment.
Implement separate classes for all the various processes through which the data goes through, 
instead of coding all the processes in just one big file.
