package ru.tesla;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TcChatMessage
{
	@SuppressWarnings("unused")
	private String	chatId;
	private Date		date;
	private String	author;
	private String	message;

	public TcChatMessage(String chatId, Date date, String author, String message)
	{
		this.chatId = chatId;
		this.date = date;
		this.author = author;
		this.message = message;
	}

	public TcChatMessage(String chatId, String autor, String message)
	{
		this(chatId, new Date(), autor, message);
	}

	public String getMessage()
	{
		return message;
	}

	public String toHtml()
	{
		return "<div class=m>" + ((new SimpleDateFormat("HH:mm:ss").format(date))) + " " + author + ": " + message
				+ "</div>";
	}

}
