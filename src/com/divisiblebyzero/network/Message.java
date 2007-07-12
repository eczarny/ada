package com.divisiblebyzero.network;

//
//  network.Message.java
//  Ada Chess
//
//  Created by Eric Czarny on April 29, 2006.
//  Copyright 2006 Divisible by Zero. All rights reserved.
//

import com.divisiblebyzero.chess.models.*;

public class Message {
	private Move move;
	private byte command;

	public static class Commands {
		public static byte CONNECT    = 1;
		public static byte DISCONNECT = 2;
		public static byte ACCEPT     = 3;
		public static byte MOVE       = 4;
	}

	public Message() {
		this.move = new Move();
		this.command = -1;
	}

	public Message(Move move, byte command) {
		this.move = move;
		this.command = command;
	}

	public Message(byte command) {
		this.move = new Move();
		this.command = command;
	}

	public Message(byte bytes[]) {
		this.parseBytes(bytes);
	}

	public Move getMove() {
		return this.move;
	}

	public void setMove(Move move) {
		this.move = move;
	}

	public byte getCommand() {
		return this.command;
	}

	public void setCommand(byte command) {
		this.command = command;
	}

	public boolean isConnect() {
		return (this.command == Commands.CONNECT);
	}

	public boolean isDisconnect() {
		return (this.command == Commands.DISCONNECT);
	}

	public boolean isAccepted() {
		return (this.command == Commands.ACCEPT);
	}

	public boolean isMove() {
		return (this.command == Commands.MOVE);
	}

	public void parseBytes(byte bytes[]) {
		this.move = new Move(bytes);
		this.command = bytes[4];
	}

	public byte[] toBytes() {
		byte[] result = new byte[5];

		result[0] = (byte)this.move.getX().getRank();
		result[1] = (byte)this.move.getX().getFile();
		result[2] = (byte)this.move.getY().getRank();
		result[3] = (byte)this.move.getY().getFile();
		result[4] = this.command;

		return result;
	}

	public String toString() {
		return "Message: (" + this.move + ", " + this.command + ")";
	}
}