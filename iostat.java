mport com.sun.btrace.annotations.*;
import com.sun.btrace.aggregation.Aggregation;
import com.sun.btrace.aggregation.AggregationKey;
import com.sun.btrace.aggregation.AggregationFunction;
import static com.sun.btrace.BTraceUtils.*;



@BTrace public class iostat {

    private static Aggregation count = Aggregations.newAggregation(AggregationFunction.COUNT);

    private static Aggregation averageRead = Aggregations.newAggregation(AggregationFunction.AVERAGE);
    private static Aggregation averageWrite = Aggregations.newAggregation(AggregationFunction.AVERAGE);

    @OnMethod(
      clazz="+java.nio.channels.FileChannel",
      method="/.*/"
    )
    public static void trace(@ProbeClassName String pcn, @ProbeMethodName String pmn) {
        AggregationKey key = Aggregations.newAggregationKey(Strings.strcat(pcn, Strings.strcat("#", pmn)));
        Aggregations.addToAggregation(count, key, 1);
    }


    @OnMethod(
      clazz="sun.nio.ch.FileChannelImpl",
      method="write",
      location = @Location(Kind.RETURN)
    )
    public static void traceWrite(@ProbeClassName String pcn, @ProbeMethodName String pmn,
            @Duration long duration, @Return int count) {

        if (count < 0) return;

        AggregationKey key = Aggregations.newAggregationKey(Strings.strcat(pcn, Strings.strcat("#", pmn)));
        Aggregations.addToAggregation(averageWrite, key, (count*976) / duration);
    }
    @OnMethod(
      clazz="sun.nio.ch.FileChannelImpl",
      method="read",
      location = @Location(Kind.RETURN)
    )
    public static void traceRead(@ProbeClassName String pcn, @ProbeMethodName String pmn,
            @Duration long duration, @Return int count) {

        if (count < 0) return;

        AggregationKey key = Aggregations.newAggregationKey(Strings.strcat(pcn, Strings.strcat("#", pmn)));
        Aggregations.addToAggregation(averageRead, key, (count*976) / duration );
    }


    @OnEvent
    @OnTimer(2000)
    public static void onEvent() {
        println("");
        println("---------------------------------------------");
        Aggregations.printAggregation("Count in 2s.", count);
        Aggregations.clearAggregation(count);

        println("");
        Aggregations.printAggregation("Average Write, MB/s", averageWrite);

        println("");
        Aggregations.printAggregation("Average Read, MB/s", averageRead);
    }
}
