import NetGraphAlgebraDefs.{NetGraph, NodeObject}
import java.io.{BufferedWriter, FileWriter}
import collection.JavaConverters._

object extract {
  def main(args: Array[String]): Unit = {
    val generateFile = new GenerateFile()
    generateFile.processFile()
  }
}
// class,
class GenerateFile {
  def processFile(): Unit = {
    // Load the ngs files for original and perturbed
    val originalGraph: Option[NetGraph] = NetGraph.load("NetGameSimimageResult.ngs", "C:\\Users\\fatjj\\Documents\\(6) UIC Fall 2023\\CS 441 Fall 2023\\NetGameSim\\")
    val perturbedGraph: Option[NetGraph] = NetGraph.load("NetGameSimimageResult.ngs.perturbed", "C:\\Users\\fatjj\\Documents\\(6) UIC Fall 2023\\CS 441 Fall 2023\\NetGameSim\\")

    originalGraph match {
      case Some(netOriginalGraph) =>
        val originalGraphNodes: java.util.Set[NodeObject] = netOriginalGraph.sm.nodes()
        val originalNodeList = originalGraphNodes.asScala.toList

        perturbedGraph match {
          case Some(netPerturbedGraph) =>
            val perturbedGraphNodes: java.util.Set[NodeObject] = netPerturbedGraph.sm.nodes()
            val perturbedNodeList = perturbedGraphNodes.asScala.toList

            // CSV writer
            val writer = new BufferedWriter(new FileWriter("C:\\Users\\fatjj\\Documents\\combinedComparison.csv"))

            // Write header to CSV
            writer.write("originalNode.id, " +
              "originalNode.children, " +
              "originalNode.props, " +
              "originalNode.currentDepth, " +
              "originalNode.propValueRange, " +
              "originalNode.maxDepth, " +
              "originalNode.maxBranchingFactor, " +
              "originalNode.maxProperties, " +
              "originalNode.storedValue, " +
              //----------------------------
              "perturbedNode.id, " +
              "perturbedNode.children, " +
              "perturbedNode.props, " +
              "perturbedNode.currentDepth, " +
              "perturbedNode.propValueRange, " +
              "perturbedNode.maxDepth, " +
              "perturbedNode.maxBranchingFactor, " +
              "perturbedNode.maxProperties, " +
              "perturbedNode.storedValue")
            writer.newLine()

            originalNodeList.foreach { originalNode =>
              perturbedNodeList.foreach { perturbedNode =>
                // Compare originalNode and perturbedNodes
                val areNodesEqual = compareNodes(originalNode, perturbedNode)
                if (areNodesEqual) {
                  // Nodes are equal, write to CSV
                  writer.write(
                    s"${originalNode.id}, " +
                      s"${originalNode.children}, " +
                      s"${originalNode.props}, " +
                      s"${originalNode.currentDepth}, " +
                      s"${originalNode.propValueRange}, " +
                      s"${originalNode.maxDepth}, " +
                      s"${originalNode.maxBranchingFactor}, " +
                      s"${originalNode.maxProperties}, " +
                      s"${originalNode.storedValue}, " +
                      //-----------------------
                      s"${perturbedNode.id}, " +
                      s"${perturbedNode.children}, " +
                      s"${perturbedNode.props}, " +
                      s"${perturbedNode.currentDepth}, " +
                      s"${perturbedNode.propValueRange}, " +
                      s"${perturbedNode.maxDepth}, " +
                      s"${perturbedNode.maxBranchingFactor}, " +
                      s"${perturbedNode.maxProperties}, " +
                      s"${perturbedNode.storedValue}") // Add properties here
                  writer.newLine()
                }
              }
            }
            writer.close()

            // Shard the combined CSV file into sections of 15 lines each
            shardCSV("C:\\Users\\fatjj\\Documents\\combinedComparison.csv", "C:\\Users\\fatjj\\Documents\\shardedCSV\\", 15)
          case None =>
            println("Error: Perturbed graph file not found or could not be loaded.")
        }
      case None =>
        println("Error: Original graph file not fond or could not be loaded.")
    }
  } // end of processFile

  def compareNodes(originalNode: NodeObject, perturbedNode: NodeObject): Boolean = {
    originalNode.props == perturbedNode.props
  }

  // Shard a CSV file into smaller sections
  def shardCSV(inputFileName: String, outputDirectory: String, shardSize: Int): Unit = {
    import scala.io.Source

    // Read the input CSV file and store  in a list.
    val source = Source.fromFile(inputFileName)
    val lines = source.getLines().toList
    source.close()

    // Calculate the number of shardss
    val numberOfShards = math.ceil(lines.size.toDouble / shardSize).toInt

    // Iterate over the shard and create separate shard files.
    (0 until numberOfShards).foreach { shardIndex =>
      // start and end indices for the current shard.
      val startIdx = shardIndex * shardSize
      val endIdx = math.min((shardIndex + 1) * shardSize, lines.size)

      // extract the lines for the current shard.
      val shard = lines.slice(startIdx, endIdx)

      // Define the filename for the current shard.
      val shardFileName = s"${outputDirectory}shard_$shardIndex.csv"

      // write the shard to a new file.
      val writer = new BufferedWriter(new FileWriter(shardFileName))

      // Write each line of the shard to the shard file.
      shard.foreach { line =>
        writer.write(line)
        writer.newLine()
      }
      // Close the writer for the current shard file.
      writer.close()
    }
  }

}
  //--------------------------------------------------------
  def simRank(originalNode: NodeObject, perturbedNode: NodeObject): Double = {
    val children = 1.0
    val props = 1.0
    val currentDepth = 1.0
    val propValueRange = 1.0
    val maxDepth = 1.0
    val maxBranchingFactor = 1.0
    val maxProperties = 1.0
    val storedValue = 1.0

    // Calculate individual similarity scores for each property
    val similarityChildren = compareProperty(originalNode.children, perturbedNode.children)
    val similarityProps = compareProperty(originalNode.props, perturbedNode.props)
    val similarityCurrentDepth = compareProperty(originalNode.currentDepth, perturbedNode.currentDepth)
    val similarityPropValueRange = compareProperty(originalNode.propValueRange, perturbedNode.propValueRange)
    val similarityMaxDepth = compareProperty(originalNode.maxDepth, perturbedNode.maxDepth)
    val similarityMaxBranchingFactor = compareProperty(originalNode.maxBranchingFactor, perturbedNode.maxBranchingFactor)
    val similarityMaxProperties = compareProperty(originalNode.maxProperties, perturbedNode.maxProperties)
    val similarityStoredValue = compareProperty(originalNode.storedValue, perturbedNode.storedValue)

    // Calculate the weighted average similarity score
    val weightedSum = (children * similarityChildren) +
      (props * similarityProps) +
      (currentDepth * similarityCurrentDepth) +
      (propValueRange * similarityPropValueRange) +
      (maxDepth * similarityMaxDepth) +
      (maxBranchingFactor * similarityMaxBranchingFactor) +
      (maxProperties * similarityMaxProperties) +
      (storedValue * similarityStoredValue)

    val totalValue =
      children + props + currentDepth + propValueRange +
        maxDepth + maxBranchingFactor + maxProperties + storedValue

    val overallSimilarityScore = weightedSum / totalValue

    overallSimilarityScore
  }

  // compare two properties and assign a score
  def compareProperty(property1: Any, property2: Any): Double = {
    // Compare property1 and property2
    if (property1 == property2) {
      1.0 // Properties are identical
    } else {
      0.0 // Properties are different
    }
}

