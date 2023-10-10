import NetGraphAlgebraDefs.{NetGraph, NodeObject}
import java.io.{BufferedWriter, FileWriter}
import collection.JavaConverters._

object extract {
  def main(args: Array[String]): Unit = {
    val generateFile = new GenerateFile()
    generateFile.processFile()
  }
}
// class
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
    // Compare properties and return true if nodes are considered equal, false otherwise
    originalNode.props == perturbedNode.props
  }

  // Shard a CSV file into smaller sections
  def shardCSV(inputFileName: String, outputDirectory: String, shardSize: Int): Unit = {
    import scala.io.Source

    val source = Source.fromFile(inputFileName)
    val lines = source.getLines().toList
    source.close()

    val numberOfShards = math.ceil(lines.size.toDouble / shardSize).toInt

    /*
      for (shardIndex <- 0 until numberOfShards) {
          val startIdx = shardIndex * shardSize
          val endIdx = math.min((shardIndex + 1) * shardSize, lines.size)
          val shard = lines.slice(startIdx, endIdx)

          val shardFileName = s"${outputDirectory}shard_$shardIndex.csv"
          val writer = new BufferedWriter(new FileWriter(shardFileName))
          shard.foreach { line =>
            writer.write(line)
            writer.newLine()
          }
          writer.close()
        }
     */

    (0 until numberOfShards).foreach { shardIndex =>
      val startIdx = shardIndex * shardSize
      val endIdx = math.min((shardIndex + 1) * shardSize, lines.size)
      val shard = lines.slice(startIdx, endIdx)

      val shardFileName = s"${outputDirectory}shard_$shardIndex.csv"
      val writer = new BufferedWriter(new FileWriter(shardFileName))
      shard.foreach { line =>
        writer.write(line)
        writer.newLine()
      }
      writer.close()
    }
  }

  def simRank(originalNode: NodeObject, perturbedNode: NodeObject): Double = {
    // Define a weight for each property (you can adjust these weights)
    val weightChildren = 1.0
    val weightProps = 1.0
    val weightCurrentDepth = 1.0
    val weightPropValueRange = 1.0
    val weightMaxDepth = 1.0
    val weightMaxBranchingFactor = 1.0
    val weightMaxProperties = 1.0
    val weightStoredValue = 1.0

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
    val weightedSum = (weightChildren * similarityChildren) +
      (weightProps * similarityProps) +
      (weightCurrentDepth * similarityCurrentDepth) +
      (weightPropValueRange * similarityPropValueRange) +
      (weightMaxDepth * similarityMaxDepth) +
      (weightMaxBranchingFactor * similarityMaxBranchingFactor) +
      (weightMaxProperties * similarityMaxProperties) +
      (weightStoredValue * similarityStoredValue)

    val totalWeight =
      weightChildren + weightProps + weightCurrentDepth + weightPropValueRange +
        weightMaxDepth + weightMaxBranchingFactor + weightMaxProperties + weightStoredValue

    val overallSimilarityScore = weightedSum / totalWeight

    overallSimilarityScore
  }

  // Method to compare two properties and assign a score
  def compareProperty(property1: Any, property2: Any): Double = {
    // Compare property1 and property2 and return a score (e.g., 0 for dissimilar, 1 for identical)
    // You can define your own logic for property comparison
    if (property1 == property2) {
      1.0 // Properties are identical
    } else {
      0.0 // Properties are dissimilar
    }
  }
}

