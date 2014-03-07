package ru.tesla;
import jabify.swing.ChatEditorKit;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class TeslaChat extends JFrame {
	private static final long serialVersionUID = -2053293821387962615L;
	TcMoveAdapter moveAdapter = new TcMoveAdapter();
	private TcChatWindow chat;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				TeslaChat win = new TeslaChat();
				win.setVisible(true);
			}
		});
	}

	public TeslaChat() throws HeadlessException {
		super();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initElements();

		chat = new TcChatWindow();
		chat.setSize(300, 400);
		chat.setVisible(true);
	}

	private void initElements() {
		setLayout(new GridLayout(0, 1));
		setTitle("Tesla.chat");
		setBounds(0, 0, 300, 300);

		JPanel p = new JPanel(null);
		// p.setBorder(BorderFactory
		// .createTitledBorder(null, "", TitledBorder.CENTER, TitledBorder.BOTTOM,
		// null, Color.yellow));
		add(p);

		JLabel jl = new JLabel("Прозрачность:");
		jl.setBounds(10, 10, 100, 20);
		p.add(jl);
		JSlider js = new JSlider(0, 100, 100);
		js.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				chat.setOpacity(((JSlider) e.getSource()).getValue() / 100.0f);
			}
		});
		js.setBounds(100, 10, 150, 20);
		p.add(js);

		p = new JPanel(null);
		add(p);

		// JLabel jl = new JLabel("Прозрачность", JLabel.RIGHT);
		// c.fill = GridBagConstraints.HORIZONTAL;
		// c.gridx = 0;
		// c.gridy = 0;
		//
		// add(p);
		//
		// JSlider js = new JSlider(0, 100, 70);
		// js.setAlignmentX(LEFT_ALIGNMENT);
		// js.setAlignmentY(TOP_ALIGNMENT);
		// js.addChangeListener(new ChangeListener() {
		// @Override
		// public void stateChanged(ChangeEvent e) {
		// chat.setOpacity(((JSlider) e.getSource()).getValue() / 100.0f);
		// }
		// });
		// c.fill = GridBagConstraints.HORIZONTAL;
		// c.gridx = 1;
		// c.gridy = 0;
		// add(js,c);
		//
		// p = new JPanel();
		// c.fill = GridBagConstraints.HORIZONTAL;
		// c.gridx = 0;
		// c.gridy = 1;
		// add(p, c);
		//
		// p.add(new JButton("sdfkj ;kldsfg;ks d;fk gjs; dfkjg ;sdfkgjs;"));
		// p.add(new JButton("sdfkj ;kldsfg;ks d;fk gjs; dfkjg ;sdfkgjs;"));
		// p.add(new JButton());
		// p.add(new JButton());
		// p.add(new JButton());
		//
		// pack();
	}

	private class TcChatWindow extends JFrame {
		private static final long serialVersionUID = -6434464177145158391L;
		private JEditorPane textPane;
		private JScrollPane jScrollPane;

		public TcChatWindow() {
			super();

			setTitle("Чатики");
			setUndecorated(true);
			setAlwaysOnTop(true);
			setOpacity(0.7f);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			textPane = new JEditorPane();
			textPane.setEditorKit(new ChatEditorKit());
			textPane.setContentType("text/html");
			textPane.setEditable(false);
			textPane.setFocusable(false);
			textPane.addMouseListener(moveAdapter);
			textPane.addMouseMotionListener(moveAdapter);
			textPane.setMargin(new Insets(0, 0, 0, 0));

			jScrollPane = new JScrollPane();
			jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
			jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			jScrollPane.setFocusable(true);
			jScrollPane.setViewportView(textPane);

			add(jScrollPane, BorderLayout.CENTER);

			String image1 = getClass().getResource("res/image1.gif").toString();
			String image2 = getClass().getResource("res/image2.gif").toString();
			
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("res/index.html")));
				StringBuilder sb = new StringBuilder();
				String temp = null;
				while ((temp = br.readLine()) != null) {
					sb.append(temp);
				}
				br.close();
				String res = sb.toString();
				res = res.replaceAll("\\{image1\\}", image1);
				res = res.replaceAll("\\{image2\\}", image2);

				textPane.setText(res);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	private static class TcMoveAdapter extends MouseAdapter {
		private boolean dragging = false;
		private boolean resizing = false;
		private int prevX = -1;
		private int prevY = -1;

		public void mousePressed(MouseEvent e) {
			if (SwingUtilities.isLeftMouseButton(e)) {
				if (!resizing)
					dragging = true;
			}
			prevX = e.getXOnScreen();
			prevY = e.getYOnScreen();
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			super.mouseMoved(e);
			boolean prev_resizing = resizing;
			Window w = SwingUtilities.getWindowAncestor(e.getComponent());
			if (w != null && w.isShowing()) {
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

		public void mouseDragged(MouseEvent e) {
			if (prevX != -1 && prevY != -1) {
				Window w = SwingUtilities.getWindowAncestor(e.getComponent());
				if (w != null && w.isShowing()) {
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

		public void mouseReleased(MouseEvent e) {
			dragging = false;
		}
	}

}
