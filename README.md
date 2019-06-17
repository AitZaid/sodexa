# Data Analysis

This project holds a little demo of Spark data analysis upon Brisbane city bike stations dataset. 

Initial instructions can be found in Test_DE.docx file.

Input data : data/raw-input/Brisbane_CityBike.json

# General idea around the industrial version of sucha a project

In the following explanation, I'll take assumptions that, of course, would have been discussed and validated with the 
business and operation teams.

## Assumptions of the workflow 

The model is trained once in a while, with a manual with a selected data set. This model is saved into HDFS for daily
prediction use. 

The model is actually not brutally configured and dropped as it is in the code, but configured by a configuration file
injected as a parameter on the job launch.

Daily data is dropped daily in the correctly formatted folder compatible fo Hive practice : 
/Project_folder/input/Y=YYYY/M=mm/D=dd/...json

Daily output is also pushed into such a project folder : 
/Project_folder/output/use_case/Y=YYYY/M=mm/D=dd/...

A cron job or a oozie configuration launches the daily jobs at a given time. 

# Building the program

$ sbt clean test package -Xdisable-assertions

# Running the program

$ spark-submit --class fr.sodexo.citybike.CityBikeClustering --master <YOUR CLUSTER IP:PORT> --deploy-mode cluster \
--conf spark.yarn.maxAppAttempts=1 target/scala-2.11/sodexo-data_2.11-0.1.jar \
/home/avi/learningspace/Sodexo-Data/data/raw-input/Brisbane_CityBike.json /home/avi/learningspace/Sodexo-Data/data/output/  

In practice, with a locally launched cluster, this can look as such : 

$ /opt/spark-2.4.1-bin-hadoop2.7/bin/spark-submit --class fr.sodexo.citybike.CityBikeClustering \ 
--master spark://arch-desktop:7077 --deploy-mode cluster --conf spark.yarn.maxAppAttempts=1 \ 
--conf spark.yarn.executor.memoryOverhead=600 target/scala-2.11/sodexo-data_2.11-0.1.jar \ 
/home/avi/learningspace/Sodexo-Data/data/raw-input/Brisbane_CityBike.json /home/avi/learningspace/Sodexo-Data/data/output/  

# Status of the project

Well, considering a rather vague specification, I've taken the following asumptions: 
- Developping the project with Spark under Scala
- Predicting only two clusters out of MLLIB Kmeans clustering with simple integer output.
- Performance was not really considered to optimize the caching or execution plan considering the little time I could spend on it
in the middle of my work day.
- Testing was not extended to datasets to demonstrate the program execution of the dat preparation.
- Clustering model was not tested at all relying only on a library (only considering data preparation)
- I output the data prepared for the model.

# TODO after

- More testing around the output,
- Plotting dat and results as an exploratory task to assess clustering, so as to consider computing the silhouette of the clusters.
- Defining a configuration file to configure model options, ... without getting into the code.
- Prepare bash files (adapted to the environment) to perform a daily task with dated data folders.
- Identify and output errors with a convenient reporting system (like email summary can be interesting)

# Code decisions

I've decided to load data in a Dataframe as it is easy to load JSON data.
I've prefered repair the data using a RDD, much easier to me to handle multi column interactions.

I've divided the task into two classes representing transformation of data, running the whole script in an object. 

I like to divide the data tasks into transformations (like data preparation, ...), each of them retaining input,
output and error references to easily reuse data results. I haven't developped though the full structure handling such 
structures. 

Using a class instance rather than an object let me make them configurable, reuse with parameters or explore several 
different configurations for models at once. 

I also often divide jobs to be able to run as a big unique job with the full task but at each significant step, output 
the data in files. This allows other tasks reuse intermediate results like it used to be in MR jobs.
 
Even if code looks a bit more clumsy using the data field names rather than the simple integers, I think it is more reliable.
 
# Time schedule

Well, I guess I've taken a bit more time than half a day honnestly. I had to reinstall a spark cluster locally (using usually an old 1.4.1).
I had several issues handling conversion of data and data types correctly. # sodexa
