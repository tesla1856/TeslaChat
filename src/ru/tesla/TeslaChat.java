package ru.tesla;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.ggl.fontchooser.view.FontChooser;

public class TeslaChat extends JFrame
{
	private static final long	serialVersionUID	= -2053293821387962615L;
	private TcChatWindow			chat;

	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				TeslaChat win = new TeslaChat();
				win.setVisible(true);
			}
		});
	}

	public TeslaChat() throws HeadlessException
	{
		super();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		chat = new TcChatWindow(this);

		initElements();

		chat.insertMessage("<div color=red align=center>WELCOME to Tesla Chat.</div>");

		chat.setSize(300, 400);
		chat.setVisible(true);
	}

	private void initElements()
	{
		setLayout(new GridLayout(0, 1));
		setTitle("Tesla.chat");
		setPreferredSize(new Dimension(210, 300));

		JPanel p = new JPanel(null);
		// -------------------------//
		int pos_y = 10;
		// p.setBorder(BorderFactory
		// .createTitledBorder(null, "", TitledBorder.CENTER, TitledBorder.BOTTOM,
		// null, Color.yellow));
		add(p);
		JLabel jl = new JLabel("Прозрачность:");
		jl.setBounds(10, pos_y, 150, 20);
		p.add(jl);
		JSlider js = new JSlider(0, 100, 0);
		js.setBounds(150, pos_y, 50, 20);
		js.addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent e)
			{
				chat.setOpacity((100 - ((JSlider) e.getSource()).getValue()) / 100.0f);
			}
		});
		p.add(js);

		// -------------------------//
		pos_y += 30;
		jl = new JLabel("Рамка:");
		jl.setBounds(10, pos_y, 150, 20);
		p.add(jl);
		JCheckBox cb = new JCheckBox("", true);
		cb.setBounds(150, pos_y, 20, 20);
		cb.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				chat.setChatHaveBorder(((JCheckBox) e.getSource()).isSelected());
				chat.UpdateChat();
			}
		});
		p.add(cb);

		// -------------------------//
		pos_y += 30;
		jl = new JLabel("Цвет фона:");
		jl.setBounds(10, pos_y, 150, 20);
		p.add(jl);
		JButton button = new JButton("");
		button.setBounds(150, pos_y, 20, 20);
		button.setMargin(new Insets(5, 5, 5, 5));
		button.setBackground(chat.getChatBgColor());
		button.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				chat.setChatBgColor(JColorChooser.showDialog(TeslaChat.this, "Choose a color", chat.getChatBgColor()));
				((JButton) e.getSource()).setBackground(chat.getChatBgColor());
				chat.UpdateChat();
			}
		});
		p.add(button);

		// -------------------------//
		pos_y += 30;
		jl = new JLabel("Цвет текста:");
		jl.setBounds(10, pos_y, 150, 20);
		p.add(jl);
		button = new JButton("");
		button.setBounds(150, pos_y, 20, 20);
		button.setMargin(new Insets(5, 5, 5, 5));
		button.setBackground(chat.getChatNormalTextColor());
		button.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				chat.setChatNormalTextColor(JColorChooser.showDialog(TeslaChat.this, "Choose a color",
						chat.getChatNormalTextColor()));
				((JButton) e.getSource()).setBackground(chat.getChatNormalTextColor());
				chat.setChatBorder(BorderFactory.createLineBorder(chat.getChatNormalTextColor()));
				chat.UpdateChat();
			}
		});
		p.add(button);

		// -------------------------//
		pos_y += 30;
		jl = new JLabel("Шрифт текста:");
		jl.setBounds(10, pos_y, 150, 20);
		p.add(jl);
		button = new JButton("F");
		button.setBounds(150, pos_y, 20, 20);
		button.setMargin(new Insets(0, 0, 0, 0));
		button.setFont(chat.getChatNormalFont());
		button.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				FontChooser jc = new FontChooser(chat, chat.getChatNormalFont(),
						"Съешь ещё этих мягких французских булок, да выпей чаю.");
				if (jc.isOkPressed())
				{
					chat.setChatNormalFont(jc.getSelectedFont());
					((JButton) e.getSource()).setFont(chat.getChatNormalFont());
					chat.UpdateChat();
				}
			}
		});
		p.add(button);
		// -------------------------//
		pos_y += 30;
		jl = new JLabel("Тест:");
		jl.setBounds(10, pos_y, 150, 20);
		p.add(jl);
		button = new JButton("х");
		button.setBounds(150, pos_y, 20, 20);
		button.setMargin(new Insets(0, 0, 0, 0));
		button.setFont(chat.getChatNormalFont());
		button.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				onReceiveMessage("TWITCH", "admin", "test ok! <a href='http://ya.ru'>yandex</a>");
			}
		});
		p.add(button);

		pack();
	}

	public void onReceiveMessage(String p_chatId, String p_author, String p_message)
	{
		onReceiveMessage(p_chatId, new Date(), p_author, p_message);
	}
	
	public void onReceiveMessage(String p_chatId, Date date, String p_author, String p_message)
	{
		chat.insertMessage("<div class=m>" + ((new SimpleDateFormat("HH:mm:ss").format(date))) + " " + p_author + ": "
				+ p_message + "</div>");
	}

}
