package numscala.ui.model;

import java.util.List;

/**
 * Created by Trung on 11/29/2017.
 */
public class DataWrapper {

    List<Double> axis;

    Double accuracy;

    public List<Double> getAxis() {
        return axis;
    }

    public void setAxis(List<Double> axis) {
        this.axis = axis;
    }

    public Double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Double accuracy) {
        this.accuracy = accuracy;
    }
}
