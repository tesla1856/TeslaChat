/*
 * Copyright before changes follows.
 *
 * @(#)LineView.java     1.12 00/02/02
 *
 * Copyright 1997-2000 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * This software is the proprietary information of Sun Microsystems, Inc.  
 * Use is subject to license terms.
 * 
 */
package jabify.swing;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.SizeRequirements;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.ParagraphView;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.View;
class ChatLineView extends ParagraphView {
	int tabBase;
	public ChatLineView(Element elem) {
		super(elem);
	}

	public boolean isVisible() {
		return true;
	}

	public float getMinimumSpan(int axis) {
		return getPreferredSpan(axis);
	}

	public int getResizeWeight(int axis) {
		switch (axis) {
			case View.X_AXIS :
				return 1;
			case View.Y_AXIS :
				return 0;
			default :
				throw new IllegalArgumentException("Invalid axis: " + axis);
		}
	}

	public float getAlignment(int axis) {
		if (axis == View.X_AXIS) {
			return 0;
		}
		return super.getAlignment(axis);
	}

	public float nextTabStop(float x, int tabOffset) {
		if (getTabSet() == null && StyleConstants.getAlignment(getAttributes()) == StyleConstants.ALIGN_LEFT) {
			return getPreTab(x, tabOffset);
		}
		return super.nextTabStop(x, tabOffset);
	}

	protected float getPreTab(float x, int tabOffset) {
		Document d = getDocument();
		View v = getViewAtPosition(tabOffset, null);
		if ((d instanceof StyledDocument) && v != null) {
			Font f = ((StyledDocument) d).getFont(v.getAttributes());
			Graphics g = getGraphics();
			FontMetrics fm = g.getFontMetrics(f);
			int width = getCharactersPerTab() * fm.charWidth('W');
			int tb = (int) getTabBase();
			return (float) ((((int) x - tb) / width + 1) * width + tb);
		}
		return 10.0f + x;
	}

	protected int getCharactersPerTab() {
		return 8;
	}

	protected SizeRequirements calculateMinorAxisRequirements(int axis, SizeRequirements r) {
		r = super.calculateMinorAxisRequirements(axis, r);
		r.minimum = 0;
		r.preferred = 0;
		r.maximum = (int) Float.MAX_VALUE;
		return r;
	}
}