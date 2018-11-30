import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._

/**
  * 计算UV  log文件的每一行为每一条访问记录,每条记录中第一个元素为访问的ip
  */
class LocalUV {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("pv").setMaster("local[2]")
    val sc = new SparkContext(conf)

    val rdd1 = sc.textFile("G:/result/access.log")

    // 分割每行数据取出IP，去重
    val rdd2 = rdd1.map(_.split(" ")).map(x => x(0)).distinct().map(x => ("uv", 1))
    rdd2.foreach(println)
  }

}