package jabify.swing;

import javax.swing.text.Element;
import javax.swing.text.LabelView;
import javax.swing.text.StyleConstants;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.CSS;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;

public class ChatEditorKit extends HTMLEditorKit
{
	private static final long	serialVersionUID	= 4003032221828561704L;

	public ChatEditorKit()
	{
		super();
	}

	public ViewFactory getViewFactory()
	{
		return new ChatFactory();
	}

	public class ChatFactory extends HTMLFactory
	{

		public View create(Element elem)
		{
			Object tag = elem.getAttributes().getAttribute(StyleConstants.NameAttribute);

			if (tag instanceof HTML.Tag)
			{
				HTML.Tag kind = (HTML.Tag) tag;

				if (kind == HTML.Tag.IMPLIED)
				{
					String ws = (String) elem.getAttributes().getAttribute(CSS.Attribute.WHITE_SPACE);
					if ((ws != null) && ws.equals("pre"))
					{
						return new ChatLineView(elem);
					}

					return new ChatParagraphView(elem);

				} else if ((kind == HTML.Tag.H1) || (kind == HTML.Tag.H2) || (kind == HTML.Tag.H3)
						|| (kind == HTML.Tag.H4) || (kind == HTML.Tag.H5) || (kind == HTML.Tag.H6) || (kind == HTML.Tag.DT))
				{
					return new ChatParagraphView(elem);
				} else
				{
					return super.create(elem);
				}
			}
			return new LabelView(elem);
		}
	}
}