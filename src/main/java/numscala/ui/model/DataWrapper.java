package numscala.ui.model;

import java.util.List;

/**
 * Created by Trung on 11/29/2017.
 */
public class DataWrapper {

    List<Double> axis;

    Double doubleInfo;

    String param;

    public List<Double> getAxis() {
        return axis;
    }

    public void setAxis(List<Double> axis) {
        this.axis = axis;
    }

    public Double getDoubleInfo() {
        return doubleInfo;
    }

    public void setDoubleInfo(Double doubleInfo) {
        this.doubleInfo = doubleInfo;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }
}
