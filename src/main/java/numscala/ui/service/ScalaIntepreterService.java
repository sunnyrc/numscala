package numscala.ui.service;

import scala.collection.JavaConversions;
import scala.collection.Seq;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trung on 11/29/2017.
 */
public class ScalaIntepreterService {

    /**
     * Parse scala Sequence as List<Double>
     * @param seq
     * @return
     */
    public List<Double> scalaSequenceToList(Seq<Object> seq) {
        List<Object> objectList = JavaConversions.seqAsJavaList(seq);
        List<Double> resultList = new ArrayList<>();

        for (Object o: objectList) {
            Double point = (Double) o;
            resultList.add(point);
        }

        return resultList;
    }

    /**
     * Convert and round accuracy to 5 digits
     * @param o
     * @return
     */
    public Double roundAccuracy(Object o) {
        Double accuracy = (Double) o;
        DecimalFormat format = new DecimalFormat("##.00000");

        return Double.parseDouble(format.format(accuracy));
    }
}
