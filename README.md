# Jose Nava

NetID: jnava35@uic.edu | UIN: 660115946 | Repo for HW 1 CS 441

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

# Requirements
1. Generate a .ngs and .perturbed files
2. Load in those files to compare original against perturbed

# Limitations
1. User working on macOS will have to setup their path's differently 
