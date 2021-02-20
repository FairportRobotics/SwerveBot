package frc.team578.robot.subsystems.swerve.motionProfiling;

import java.util.ArrayList;
import frc.team578.robot.commands.*;

public class Points{
	public static final double CURVES_PER_SECOND = 0.4;
	public static final int POINTS_PER_CURVE = 30;
	public static final double[] pidValues = {0, 0.0, 0};;

	protected static class TimedCommand{
		public String name;
		public double t;

		protected TimedCommand(String name, double t){
			this.name = name;
			this.t = t;
		}
		public double getT(){
			return t;
		}
		public String getName(){
			return name;
		}
	}

	public static TimedCommand[] commands = {};
	private static class p0{
		private static double[] getPoints0(){
			double[] d = {9.963099479675293, 10.0, 0.0, 10.060722351074219, 10.0, 0.10443541293748161, 10.15850830078125, 10.0, 0.20887082587496322, 10.256458282470703, 10.0, 0.31330623881244485, 10.354571342468262, 10.0, 0.41774165174992645, 10.452849388122559, 10.0, 0.522177064687408, 10.551291465759277, 10.0, 0.6266124776248897, 10.649896621704102, 10.0, 0.7310478905623713, 10.74866771697998, 10.0, 0.8354833034998529, 10.847601890563965, 10.0, 0.9399187164373345, 10.946699142456055, 10.0, 1.044354129374816, 11.045961380004883, 10.0, 1.1487895423122976, 11.145387649536133, 10.0, 1.2532249552497794, 11.244976997375488, 10.0, 1.357660368187261, 11.344732284545898, 10.0, 1.4620957811247426, 11.444650650024414, 10.0, 1.5665311940622242, 11.544732093811035, 10.0, 1.6709666069997058, 11.644977569580078, 10.0, 1.7754020199371874, 11.74538803100586, 10.0, 1.879837432874669, 11.845961570739746, 10.0, 1.9842728458121506, 11.946699142456055, 10.0, 2.088708258749632, 12.047602653503418, 10.0, 2.1931436716871135, 12.14866828918457, 10.0, 2.297579084624595, 12.249897956848145, 10.0, 2.402014497562077, 12.35129165649414, 10.0, 2.506449910499559, 12.452850341796875, 10.0, 2.6108853234370404, 12.554573059082031, 10.0, 2.715320736374522, 12.656457901000977, 10.0, 2.8197561493120036, 12.758508682250977, 10.0, 2.924191562249485, 12.860721588134766, 10.0, 3.0286269751869668, 12.96310043334961, 10.0, 3.14};
			return d;
		}
		public static double[] getTotalPoints(){
			double[] d0 = getPoints0();
			double[] d = new double[d0.length];
			ArrayList<double[]> dd = new ArrayList<double[]>();
			dd.add(d0);
			int ind = 0;
			for(int i = 0; i < dd.size(); i++){
				double[] ddd = dd.get(i);
				for(int j = 0; j < ddd.length; j++){
					d[ind] = ddd[j];
					ind++;
				}
			}
			return d;
		}
	}
	public static double[] getTotalPoints(){
		double[] d0 = p0.getTotalPoints();
		double[] d = new double[d0.length];
		ArrayList<double[]> dd = new ArrayList<double[]>();
		dd.add(d0);
		int ind = 0;
		for(int i = 0; i < dd.size(); i++){
			double[] ddd = dd.get(i);
			for(int j = 0; j < ddd.length; j++){
				d[ind] = ddd[j];
				ind++;
			}
		}
		return d;
	}
}
