import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

/*
 * 使用Spark-Submit提交任务
 * bin/spark-submit --master spark://bigdata111:7077 --class demo.MyTomcatLog /root/temp/demo.jar
 */
object MyTomcatLog {
  def main(args: Array[String]): Unit = {
    //创建SparkContext对象
    //val conf = new SparkConf().setAppName("MyTomcatLog").setMaster("local")  // -----> 以本地模式运行
    val conf = new SparkConf().setAppName("MyTomcatLog")    // -------> 以集群模式运行
    val sc = new SparkContext(conf)

    //读入HDFS的数据
    //日志：192.168.88.1 - - [30/Jul/2017:12:54:42 +0800] "GET /MyDemoWeb/web.jsp HTTP/1.1" 200 239
    //解析完成后，返回：(web.jsp,1)  每个jsp记一次数
    val rdd1 = sc.textFile("hdfs://192.168.157.111:9000/tomcatlog/localhost_access_log.2017-07-30.txt").map(
      line =>{
        //解析出，网页的名字，并且每个网页记一次数
        //第一步：解析出双引号之间的  GET /MyDemoWeb/web.jsp HTTP/1.1
        val index1 = line.indexOf("\"")
        val index2 = line.lastIndexOf("\"")
        val line1 = line.substring(index1+1, index2)

        //第二步：解析出两个空格之间的内容：/MyDemoWeb/web.jsp
        val index3 = line1.indexOf(" ")
        val index4 = line1.lastIndexOf(" ")
        val line2 = line1.substring(index3+1, index4)

        //得到jsp的名字
        val jspName = line2.substring(line2.lastIndexOf("/")+1)

        //返回：每个网页记一次数
        (jspName,1)
      }
    )

    //把jspName相同的计数，执行累加
    //得到每个jsp的访问总量  --->  (jspName,访问量)
    val rdd2 = rdd1.reduceByKey(_+_)

    //需求：分析网站访问日志中，访问量最高的两个网页的名字
    //排序，按照访问量进行降序排序
    val rdd3 = rdd2.sortBy(_._2,false)

    //直接输出到屏幕
    println(rdd3.take(2).toBuffer)

    //停止上下文对象
    sc.stop()
  }
}

