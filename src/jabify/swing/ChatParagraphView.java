package jabify.swing;

import javax.swing.SizeRequirements;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.ParagraphView;

public class ChatParagraphView extends ParagraphView {

	public ChatParagraphView(Element elem) {
		super(elem);
	}

	protected SizeRequirements calculateMinorAxisRequirements(int axis, SizeRequirements r) {
		r = super.calculateMinorAxisRequirements(axis, r);
		r.minimum = 0;
		r.maximum = (int) Float.MAX_VALUE;
		return r;
	}
}