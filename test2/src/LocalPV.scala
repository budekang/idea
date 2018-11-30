import org.apache.spark.{SparkConf, SparkContext}
/**
  * 计算pv  log日志文件中每一行记录，为一次点击记录，也就是一次pv操作
  */
object LocalPV {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("pv").setMaster("local[2]")
    val sc = new SparkContext(conf)

    val rdd1 = sc.textFile("G:/result/access.log")
    val rdd2 = rdd1.map(x => ("pv", 1))
    val rdd3 = rdd2.reduceByKey(_+_)
    val rdd4 = rdd3.saveAsTextFile("G:/result/access1.log")

    sc.stop()
  }

}