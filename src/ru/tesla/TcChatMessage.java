package ru.tesla;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TcChatMessage
{
	private static int	global_id	= 0;
	private int					id;
	private String			chatId;
	private Date				date;
	private String			author;
	private String			message;

	public TcChatMessage(String chatId, Date date, String author, String message)
	{
		global_id++;
		id = global_id;
		this.chatId = chatId;
		this.date = date;
		this.author = author;
		this.message = message;
	}

	public String getId()
	{
		return "msg_" + id;
	}

	public String getChatId()
	{
		return chatId;
	}

	public TcChatMessage(String chatId, String autor, String message)
	{
		this(chatId, new Date(), autor, message);
	}

	public TcChatMessage(String message)
	{
		this(null, null, null, message);
	}

	public String getMessage()
	{
		return message;
	}

	public String toHtml()
	{
		return "<div class=m id='" + getId()
				+ "'>"//
				+ (date != null ? ((new SimpleDateFormat("HH:mm:ss").format(date))) + " " : "")
				+ (author != null ? author + ": " : "") //
				+ message//
				+ "</div>";
	}
}
