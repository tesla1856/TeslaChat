package ru.tesla;

import jabify.swing.ChatEditorKit;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.html.HTMLDocument;

class TcChatWindow extends JFrame
{
	private static final long	serialVersionUID		= -6434464177145158391L;

	private JEditorPane				textPane;
	private JScrollPane				jScrollPane;
	private TcMoveAdapter			moveAdapter					= new TcMoveAdapter();
	private ChatEditorKit			kit									= new ChatEditorKit();

	private Color							chatBgColor					= Color.black;
	private Color							chatNormalTextColor	= Color.white;
	private Font							chatNormalFont			= new Font("Franklin Gothic Medium Cond", Font.PLAIN, 12);
	private boolean						chatHaveBorder			= true;
	private Border						chatBorder;

	public Color getChatBgColor()
	{
		return chatBgColor;
	}
	public void setChatBorder(Border chatBorder)
	{
		this.chatBorder = chatBorder;
	}
	public Color getChatNormalTextColor()
	{
		return chatNormalTextColor;
	}
	public Font getChatNormalFont()
	{
		return chatNormalFont;
	}

	public void setChatNormalFont(Font chatNormalFont)
	{
		this.chatNormalFont = chatNormalFont;
	}

	public void setChatBgColor(Color chatBgColor)
	{
		this.chatBgColor = chatBgColor;
	}

	public void setChatNormalTextColor(Color chatNormalTextColor)
	{
		this.chatNormalTextColor = chatNormalTextColor;
	}

	public void setChatHaveBorder(boolean chatHaveBorder)
	{
		this.chatHaveBorder = chatHaveBorder;
	}

	public TcChatWindow(TeslaChat teslaChat)
	{
		super();

		setTitle("Чатики");
		setUndecorated(true);
		setAlwaysOnTop(true);
		setOpacity(1.0f);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		textPane = new JEditorPane();
		textPane.setEditorKit(kit);
		textPane.setContentType("text/html");
		textPane.setEditable(false);
		textPane.setFocusable(false);
		textPane.setAutoscrolls(true);
		textPane.setMargin(new Insets(0, 0, 0, 0));

		textPane.addMouseListener(moveAdapter);
		textPane.addMouseMotionListener(moveAdapter);

		jScrollPane = new JScrollPane();
		chatBorder = BorderFactory.createLineBorder(chatNormalTextColor);
		jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollPane.setFocusable(true);
		jScrollPane.setViewportView(textPane);

		add(jScrollPane);

		UpdateChat();

	}

	public void UpdateChat()
	{
		String image1 = getClass().getResource("res/image1.gif").toString();
		String image2 = getClass().getResource("res/image2.gif").toString();

		try
		{
			if (chatHaveBorder)
				jScrollPane.setBorder(chatBorder);
			else
				jScrollPane.setBorder(null);

			BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("res/index.html"),
					"windows-1251"));
			StringBuilder sb = new StringBuilder();
			String temp = null;
			while ((temp = br.readLine()) != null)
			{
				sb.append(temp);
			}
			br.close();
			String res = sb.toString();
			String style = "body {";
			style += " margin: 2px;";
			style += " background: "
					+ String.format("#%02x%02x%02x", chatBgColor.getRed(), chatBgColor.getGreen(), chatBgColor.getBlue()) + ";";
			style += " color: "
					+ String.format("#%02x%02x%02x", chatNormalTextColor.getRed(), chatNormalTextColor.getGreen(),
							chatNormalTextColor.getBlue()) + ";";
			style += " font: " + (chatNormalFont.getStyle() == Font.ITALIC ? "italic" : "normal") + " "
					+ chatNormalFont.getSize() + "pt \"" + chatNormalFont.getFamily() + "\";";
			style += " font-weight: " + (chatNormalFont.getStyle() == Font.BOLD ? "bold" : "normal") + ";";
			style += "}";
			style += ".m {text-indent: -20; margin-left: 20; margin-top: 0; text-indent: -20; clear: both;}";

			res = res.replaceAll("_style_", style);
			res = res.replaceAll("\\{image1\\}", image1);
			res = res.replaceAll("\\{image2\\}", image2);

			textPane.setText(res);

		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	private static class TcMoveAdapter extends MouseAdapter
	{
		private boolean	dragging	= false;
		private boolean	resizing	= false;
		private int			prevX			= -1;
		private int			prevY			= -1;

		public void mousePressed(MouseEvent e)
		{
			if (SwingUtilities.isLeftMouseButton(e))
			{
				if (!resizing)
					dragging = true;
			}
			prevX = e.getXOnScreen();
			prevY = e.getYOnScreen();
		}

		@Override
		public void mouseMoved(MouseEvent e)
		{
			super.mouseMoved(e);
			boolean prev_resizing = resizing;
			Window w = SwingUtilities.getWindowAncestor(e.getComponent());
			if (w != null && w.isShowing())
			{
				Rectangle rect = w.getBounds();
				if (rect.width + rect.x - 10 < (e.getXOnScreen()) && rect.height + rect.y - 10 < (e.getYOnScreen()))
					resizing = true;
				else
					resizing = false;

				if (prev_resizing != resizing)
					if (resizing)
						w.setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
					else
						w.setCursor(Cursor.getDefaultCursor());
			}
		}

		public void mouseDragged(MouseEvent e)
		{
			if (prevX != -1 && prevY != -1)
			{
				Window w = SwingUtilities.getWindowAncestor(e.getComponent());
				if (w != null && w.isShowing())
				{
					Rectangle rect = w.getBounds();
					if (dragging)
						w.setBounds(rect.x + (e.getXOnScreen() - prevX), rect.y + (e.getYOnScreen() - prevY), rect.width,
								rect.height);
					else if (resizing)
						w.setBounds(rect.x, rect.y, rect.width + (e.getXOnScreen() - prevX), rect.height
								+ (e.getYOnScreen() - prevY));

				};
				prevX = e.getXOnScreen();
				prevY = e.getYOnScreen();
			}
		}

		public void mouseReleased(MouseEvent e)
		{
			dragging = false;
		}
	}

	public void insertMessage(String p_message)
	{
		HTMLDocument doc = (HTMLDocument) textPane.getDocument();
		Element end = (doc).getElement("END_OF_MESSAGES");
		try
		{
			kit.insertHTML(doc, end.getStartOffset(), p_message, 0, 0, null);
			textPane.setCaretPosition(textPane.getDocument().getLength());

		} catch (BadLocationException | IOException e)
		{
			e.printStackTrace();
		}
	}

}