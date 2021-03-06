package mobi.ioio.plotter.shapes;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;

import mobi.ioio.plotter.Curve;

public class Line extends Curve {
	private final double totalTime_;
	private final float from_[];
	private final float to_[];

    public Line(float from[], float to[], double mmPerSec) {
        assert from.length == 2;
        assert to.length == 2;

        totalTime_ = Math.hypot(to[0] - from[0], to[1] - from[1]) / mmPerSec;
        from_ = from.clone();
        to_ = to.clone();
    }
    public Line(float fromx, float fromy, float tox, float toy, double mmPerSec) {
        totalTime_ = Math.hypot(tox - fromx, toy - fromy) / mmPerSec;
        from_ = new float[] { fromx, fromy };
        to_ = new float[] { tox, toy };
    }

    @Override
	public double totalTime() {
		return totalTime_;
	}

	@Override
	public void getPosTime(double time, float[] xy) {
		assert xy.length == 2;

		final double ratio = limit(time / totalTime_, 0, 1);
		xy[0] = (float) (to_[0] * ratio + from_[0] * (1 - ratio));
		xy[1] = (float) (to_[1] * ratio + from_[1] * (1 - ratio));
	}

    @Override
    public float[] getBounds() {
        return new float[] {
                Math.min(from_[0], to_[0]),
                Math.min(from_[1], to_[1]),
                Math.max(from_[0], to_[0]),
                Math.max(from_[1], to_[1])
        };
    }

    @Override
    public void renderToMat(Mat mat, Scalar color) {
        Core.line(mat, new Point(from_[0], from_[1]), new Point(to_[0], to_[1]), color);
    }

    private static double limit(double val, double min, double max) {
		if (val < min)
			return min;
		if (val > max)
			return max;
		return val;
	}
}
