/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
 * 
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 *
 * Original Author:  Arnaud Roques
 * 
 *
 */
package net.sourceforge.plantuml.creole;

import java.awt.Color;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorSimple;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.math.ScientificEquationSafe;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UImage;
import net.sourceforge.plantuml.ugraphic.UImageSvg;

public class AtomMath implements Atom {

	private final double scale;
	private final ScientificEquationSafe math;
	private final HtmlColor foreground;
	private final HtmlColor background;

	public AtomMath(ScientificEquationSafe math, HtmlColor foreground, HtmlColor background, double scale) {
		this.math = math;
		this.foreground = foreground;
		this.background = background;
		this.scale = scale;
	}

	private Dimension2D calculateDimensionSlow(StringBounder stringBounder) {
		final BufferedImage image = math.getImage(scale, Color.BLACK, Color.WHITE);
		return new Dimension2DDouble(image.getWidth(), image.getHeight());
	}

	private Dimension2D dim;

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		if (dim == null) {
			dim = calculateDimensionSlow(stringBounder);
		}
		return dim;
	}

	public double getStartingAltitude(StringBounder stringBounder) {
		return 0;
	}

	public void drawU(UGraphic ug) {
		final boolean isSvg = ug.matchesProperty("SVG");
		final Color back;
		if (isSvg && background == null) {
			back = null;
		} else {
			back = getColor(background == null ? ug.getParam().getBackcolor() : background, Color.WHITE);
		}
		final Color fore = getColor(foreground, Color.BLACK);
		if (isSvg) {
			final String svg = math.getSvg(fore, back);
			ug.draw(new UImageSvg(svg));
		} else {
			ug.draw(new UImage(math.getImage(scale, fore, back)));
		}
	}

	private Color getColor(HtmlColor color, Color defaultValue) {
		if (color instanceof HtmlColorSimple) {
			return ((HtmlColorSimple) color).getColor999();
		}
		return defaultValue;

	}
}
