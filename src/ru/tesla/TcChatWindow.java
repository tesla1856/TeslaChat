package ru.tesla;

import jabify.swing.ChatEditorKit;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.html.HTMLDocument;

class TcChatWindow extends JFrame
{
	private static final long			serialVersionUID		= -6434464177145158391L;

	private JEditorPane						textPane;
	private JScrollPane						jScrollPane;
	private TcMoveAdapter					moveAdapter					= new TcMoveAdapter();
	private ChatEditorKit					kit									= new ChatEditorKit();

	private Deque<TcChatMessage>	messages						= new ArrayDeque<TcChatMessage>();
	private int										max_msg_count				= 100;

	private Color									chatBgColor					= Color.black;
	private Color									chatNormalTextColor	= Color.white;
	private Font									chatNormalFont			= new Font("Tahoma", Font.BOLD, 11);
	private boolean								chatHaveBorder			= true;
	private Border								chatBorder;

	private KeyListener						keyListener					= new TcKeyListener();

	public int getMax_msg_count()
	{
		return max_msg_count;
	}

	public void setMax_msg_count(int max_msg_count)
	{
		this.max_msg_count = max_msg_count;
	}

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
		textPane.setFocusable(true);
		textPane.requestFocusInWindow();
		textPane.setAutoscrolls(true);
		textPane.setMargin(new Insets(0, 0, 0, 0));

		textPane.addMouseListener(moveAdapter);
		textPane.addMouseMotionListener(moveAdapter);
		textPane.addKeyListener(keyListener);
		textPane.addHyperlinkListener(new HyperlinkListener()
		{
			public void hyperlinkUpdate(HyperlinkEvent e)
			{
				if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
				{
					if (Desktop.isDesktopSupported())
					{
						try
						{
							Desktop.getDesktop().browse(e.getURL().toURI());
						} catch (IOException | URISyntaxException e1)
						{
							e1.printStackTrace();
						}
					}
				}
			}
		});

		jScrollPane = new JScrollPane();
		chatBorder = BorderFactory.createLineBorder(chatNormalTextColor);
		jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollPane.setFocusable(true);
		jScrollPane.setViewportView(textPane);

		add(jScrollPane);

		UpdateChat();

	}

	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
		Rectangle r = g.getClipBounds();
		g.setColor(chatNormalTextColor);
		for (int i = 0; i <= 12; i += 2)
			g.drawLine(r.width - i, r.height, r.width, r.height - i);
	}

	public void UpdateChat()
	{
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
			//
			style += " background: "
					+ String.format("#%02x%02x%02x", chatBgColor.getRed(), chatBgColor.getGreen(), chatBgColor.getBlue()) + ";";
			style += " color: "
					+ String.format("#%02x%02x%02x", chatNormalTextColor.getRed(), chatNormalTextColor.getGreen(),
							chatNormalTextColor.getBlue()) + ";";
			style += " font: " + (chatNormalFont.getStyle() == Font.ITALIC ? "italic" : "normal") + " "
					+ chatNormalFont.getSize() + "pt \"" + chatNormalFont.getFamily() + "\";";
			style += " font-weight: " + (chatNormalFont.getStyle() == Font.BOLD ? "bold" : "normal") + ";";
			style += "}";
			//
			style += ".m {text-indent: -20; margin-left: 20; margin-top: 0; text-indent: -20; clear: both;}";
			//
			style += "a {color: "
					+ String.format("#%02x%02x%02x", chatNormalTextColor.getRed(), chatNormalTextColor.getGreen(),
							chatNormalTextColor.getBlue()) + ";}";

			res = res.replaceAll("_style_", style);

			String msg_html = "";
			for (Iterator<TcChatMessage> i = messages.iterator(); i.hasNext();)
				msg_html += i.next().toHtml();
			res = res.replaceAll("_messages_", msg_html);

			textPane.setText(res);

		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	private class TcMoveAdapter extends MouseAdapter
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

			if (SwingUtilities.isMiddleMouseButton(e))
				insertMessage(new TcChatMessage("<hr style='width:50%;'/>"));

			prevX = e.getXOnScreen();
			prevY = e.getYOnScreen();
		}

		@Override
		public void mouseMoved(MouseEvent e)
		{
			super.mouseMoved(e);

			Window w = SwingUtilities.getWindowAncestor(e.getComponent());
			if (w != null && w.isShowing())
			{
				Rectangle rect = w.getBounds();
				if (rect.width + rect.x - 10 < (e.getXOnScreen()) && rect.height + rect.y - 10 < (e.getYOnScreen()))
					resizing = true;
				else
					resizing = false;

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
					if (dragging && (e.getModifiers() & InputEvent.CTRL_MASK) == InputEvent.CTRL_MASK)
						w.setBounds(rect.x + (e.getXOnScreen() - prevX), rect.y + (e.getYOnScreen() - prevY), rect.width,
								rect.height);
					else
					{
						int new_w = rect.width + (e.getXOnScreen() - prevX);
						int new_h = rect.height + (e.getYOnScreen() - prevY);
						if (resizing && new_w > 10 && new_h > 10)
							w.setBounds(rect.x, rect.y, new_w, new_h);
					}
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

	private class TcKeyListener implements KeyListener
	{

		@Override
		public void keyPressed(KeyEvent e)
		{
			if ((e.getModifiers() & InputEvent.CTRL_MASK) == InputEvent.CTRL_MASK
					&& jScrollPane.getVerticalScrollBarPolicy() != JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED)
			{
				jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				repaint();
			}
		}

		@Override
		public void keyReleased(KeyEvent e)
		{
			if ((e.getModifiers() & InputEvent.CTRL_MASK) != InputEvent.CTRL_MASK)
			{
				jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
				repaint();
			}
		}

		@Override
		public void keyTyped(KeyEvent e)
		{
		}

	}

	public void insertMessage(TcChatMessage msg)
	{
		messages.add(msg);
		if (messages.size() > max_msg_count)
		{
			TcChatMessage old = messages.pollFirst();
			if (old != null)
			{
				HTMLDocument doc = (HTMLDocument) textPane.getDocument();
				Element elem = doc.getElement(old.getId());
				doc.removeElement(elem);
			}
		}

		HTMLDocument doc = (HTMLDocument) textPane.getDocument();
		Element end = (doc).getElement("END_OF_MESSAGES");

		try
		{
			kit.insertHTML(doc, end.getStartOffset(), msg.toHtml(), 0, 0, null);
		} catch (BadLocationException | IOException e)
		{
			e.printStackTrace();
		}
		textPane.setCaretPosition(textPane.getDocument().getLength());
	}

}