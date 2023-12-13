import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

object WordCount {
	def main(args: Array[String]): Unit = {
		// 创建SparkConf并设置应用程序名称
		val sparkConf = new SparkConf().setAppName("SparkStreamingExample")

		// 创建StreamingContext，每秒一个批次
		val streamingContext = new StreamingContext(sparkConf, Seconds(60))

		// 从TCP socket源接收数据流
		val lines = streamingContext.socketTextStream("localhost", 9999)

		// 对接收到的数据进行处理
		val words = lines.flatMap(_.split(" "))
		val wordCounts = words.map(word => (word, 1)).reduceByKey(_ + _)

		// 打印处理结果
		wordCounts.print()

		// 启动流式处理
		streamingContext.start()

		// 等待处理结束
		streamingContext.awaitTermination()
	}
}
