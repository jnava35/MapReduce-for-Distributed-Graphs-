# Jose Nava

NetID: jnava35@uic.edu | UIN: 660115946 | Repo for HW1 CS 441

Setting up Project
1. Download repo from git
2. Open project in intelliJ
3. From the terminal, run `sbt clean compile`

Running project 
1. Once project is opened go to scr->main->scala->shard.scala
2. In shard.scala is the implementation for creating csv and sharding csv
3. Change path for "val originalGraph" and "val perturbedGraph" on lines 15, 16 to Example: `C:\\Users\\your-user-profile\\your-directory\\NetGameSim\\`
4. Change path for "val writer" on line 29 Example: `C:\\Users\\your-user-profile\\your-directory\\combinedComparison.csv`
5. Create a new folder called `shardedCSV` outside of the program, anywhere in file explorer
6. Change path for shardCVS() on line 86 to the path of step (4). Then add the path of where you created the shardedCSV folder
7. To start running program on intelliJ there will be a green run arrow on the left for line 6 for def main(). Press on the green arrow to run program
8. Open `combinedComparison.csv` to view all original vs perturbed nodes data in a single csv
9. Open `shardedCSV` folder to view all sharded original vs perturbed node data distributed in multiple csv files
10. Requirements 5-8 not yet completed

# Requirements
1. Generate a .ngs and .perturbed files
2. Load in those files to compare original against perturbed 
3. Create a csv file of original and perturbed data node
4. Shard the file into smaller sections
5. Create a simRank algorithm to figure out which comparison of original and perturbed graph are similar based on there properties
6. Create the mapper and the reducer for each task
7. Create and run your software application using Apache Hadoop to test mapper/reducer
8. Deploy it and run it on the Amazon Elastic MapReduce (EMR)
9. More info on project here: [HW1 Main Textbok Group](https://github.com/0x1DOCD00D/CS441_Fall2023/blob/main/Homework1.md#the-goal-of-this-homework-is-for-students-to-gain-experience-with-solving-a-distributed-computational-problem-using-cloud-computing-technologies-the-main-textbook-group-option-1-will-design-and-implement-an-instance-of-the-mapreduce-computational-model-using-aws-emr-whereas-the-alternative-textbook-group-option-2-will-use-the-corba-model-you-can-check-your-textbook-option-in-the-corresponding-column-of-the-gradebook-on-the-blackboard)

# Limitations
1. User working on macOS will have to setup their path's differently 
