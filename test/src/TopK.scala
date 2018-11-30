import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
/**
  * 求topk  log日志中每一行会有一个来源网址信息，统计所有日志中访问量排序并列出其中的k个
  */
object TopK {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("pv").setMaster("local[2]")
    val sc = new SparkContext(conf)

    val rdd1 = sc.textFile("E:/result/access.log")

    val rdd2 = rdd1.map(_.split(" ")).map(x => x(10)).map(f = (_, 1))
    val result = rdd2.reduceByKey(_ + _).sortBy(_._2, false).take(3)
    println(result.toList)
  }

}