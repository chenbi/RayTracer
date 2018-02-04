package colormodels;

import java.awt.Color;
import datastructures.IntersectionData;

public interface ColorModel {
	public Color getColorAt(IntersectionData intersection);
}
